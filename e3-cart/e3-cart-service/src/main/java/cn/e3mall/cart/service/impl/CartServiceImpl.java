package cn.e3mall.cart.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;


@Service
public class CartServiceImpl implements CartService {
	
	@Autowired
	private JedisClient jedisClient;

	@Value("${REDIS_CART_PRE}")
	private String REDIS_CART_PRE;
	
	@Autowired
	private TbItemMapper itemMapper;
	@Override
	public E3Result addCart(long userId, long itemId, int num) {
		Boolean hexists = jedisClient.hexists(REDIS_CART_PRE + ":" + userId, ""+ itemId);
		if(hexists) {
			String json = jedisClient.hget(REDIS_CART_PRE + ":" + userId, ""+ itemId);
			TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
			item.setNum(item.getNum() + num);
			jedisClient.hset(REDIS_CART_PRE + ":" + userId, ""+ itemId, JsonUtils.objectToJson(item));
			return E3Result.ok();
		}
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		item.setNum(num);
		String image = item.getImage();
		if(StringUtils.isNotBlank(image)) {
			item.setImage(image.split(",")[0]);
		}
		jedisClient.hset(REDIS_CART_PRE + ":" + userId, ""+ itemId, JsonUtils.objectToJson(item));
		return E3Result.ok(); 
	}
	@Override
	public E3Result mergeCart(long userId, List<TbItem> itemList) {
		for (TbItem tbItem : itemList) {
			addCart(userId, tbItem.getId(), tbItem.getNum());
		}
		return E3Result.ok();
	}
	@Override
	public List<TbItem> getCartList(long userId) {
		List<String> jsonList = jedisClient.hvals(REDIS_CART_PRE + ":" + userId);
		List<TbItem> itemList = new ArrayList<>();
		for (String s : jsonList) {
			TbItem item = JsonUtils.jsonToPojo(s, TbItem.class);
			itemList.add(item);
		}
		return itemList;
	}
	
	@Override
	public E3Result updateCartNum(long userId, long itemId, int num) {
		String json = jedisClient.hget(REDIS_CART_PRE + ":" + userId, ""+ itemId);
		TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
		item.setNum(num);
		jedisClient.hset(REDIS_CART_PRE + ":" + userId, ""+ itemId, JsonUtils.objectToJson(item));
		return E3Result.ok();
	}
	
	@Override
	public E3Result deleteCartItem(long userId, long itemId) {
		jedisClient.hdel(REDIS_CART_PRE + ":" + userId, ""+ itemId);
		return E3Result.ok();
	}
	@Override
	public E3Result clearCartItem(long userId) {
		jedisClient.del(REDIS_CART_PRE + ":" + userId);
		return E3Result.ok();
	}
	
	

}
