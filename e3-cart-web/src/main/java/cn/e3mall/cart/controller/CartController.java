package cn.e3mall.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.service.ItemService;

/**
 * 购物车处理
 * @author Administrator
 *
 */

@Controller
public class CartController {
	
	@Autowired
	private ItemService itemService;
	@Value("${COOKIE_CART_EXPIRE}")
	private Integer COOKIE_CART_EXPIRE;
	@Autowired
	private CartService cartService;
	
	@RequestMapping("/cart/add/{itemId}")
	public String addCart(@PathVariable Long itemId, @RequestParam(defaultValue="1") Integer num,
			HttpServletRequest request, HttpServletResponse response) {
		// 判断用户是否登陆
		TbUser tbUser = (TbUser) request.getAttribute("user");
		if(null != tbUser) {
			// 写入redis
			cartService.addCart(tbUser.getId(), itemId, num);
			return "cartSuccess"; 
		}
		/**
		 * 1.从cookie中取出购物车列表
		 * 2.判断商品在商品列表中是否存在
		 * 3.如果存在数量相加
		 * 4.如果不存在，根据商品id查询信息。得到一个TbItem对象
		 * 5.把商品信息添加到商品列表
		 * 6.写入cookie
		 * 7.返回添加成功页面
		 */
		List<TbItem> list = getCartListFromCookie(request);
		boolean flag = false;
		for (TbItem tbItem : list) {
			if (tbItem.getId() == itemId.longValue()) {
				flag = true;
				tbItem.setNum(tbItem.getNum()+num);
				break;
			}
		}
		if (!flag) {
			TbItem item = itemService.getItemById(itemId);
			item.setNum(num);
			String image = item.getImage();
			if(StringUtils.isNotBlank(image)) {
				item.setImage(image.split(",")[0]);
			}
			list.add(item);
		}
		
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(list), COOKIE_CART_EXPIRE, true);
		return "cartSuccess";
	}
	
	private List<TbItem> getCartListFromCookie(HttpServletRequest request) {
		String json = CookieUtils.getCookieValue(request, "cart", true);
		if(StringUtils.isBlank(json)) {
			return new ArrayList<>();
		}
		List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
		return list;
	}
	
	@RequestMapping("/cart/cart")
	public String showCartList(HttpServletRequest request, HttpServletResponse response) {
		List<TbItem> cartList = getCartListFromCookie(request);
		// 判断用户是否登陆
		TbUser tbUser = (TbUser) request.getAttribute("user");
		if(null != tbUser) {
			cartService.mergeCart(tbUser.getId(), cartList);
			
			CookieUtils.deleteCookie(request, response, "cart");
			
			cartList = cartService.getCartList(tbUser.getId());
		}
		request.setAttribute("cartList", cartList);
		return "cart";
	}
	
	@RequestMapping("/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public E3Result updateCartNum(@PathVariable Long itemId, @PathVariable Integer num,
			HttpServletRequest request, HttpServletResponse response) {
		// 判断用户是否登陆
		TbUser tbUser = (TbUser) request.getAttribute("user");
		if(null != tbUser) {
			cartService.updateCartNum(tbUser.getId(), itemId, num);
			return E3Result.ok();
		}
		/**
		 * 1.从cookie中取出购物车列表
		 * 2.遍历商品列表找到对应商品
		 * 3.更新数量
		 * 4.把购物车列表写回cookie
		 * 5.返回成功
		 */
		List<TbItem> list = getCartListFromCookie(request);
		for (TbItem tbItem : list) {
			if(tbItem.getId().longValue() == itemId) {
				tbItem.setNum(num);
				break;
			}
		}
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(list), COOKIE_CART_EXPIRE, true);
		return E3Result.ok();
	}
	
	@RequestMapping("/cart/delete/{itemId}")
	public String deleteCartItem(@PathVariable Long itemId, HttpServletRequest request, HttpServletResponse response) {
		// 判断用户是否登陆
		TbUser tbUser = (TbUser) request.getAttribute("user");
		if(null != tbUser) {
			cartService.deleteCartItem(tbUser.getId(), itemId);
			return "redirect:/cart/cart.html";
		}
		List<TbItem> list = getCartListFromCookie(request);
		for (TbItem tbItem : list) {
			if(tbItem.getId().longValue() == itemId) {
				list.remove(tbItem);
				break;
			}
		}
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(list), COOKIE_CART_EXPIRE, true);
		return "redirect:/cart/cart.html";
	}
}
