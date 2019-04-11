package com.tensquare.user.service;

import com.tensquare.user.dao.UserDao;
import com.tensquare.user.pojo.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 服务层
 * 
 * @author Administrator
 *
 */
@Service
public class UserService {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private IdWorker idWorker;
	@Autowired
	private RedisTemplate redisTemplate;
	@Autowired
	private RabbitTemplate rabbitTemplate;
	@Autowired
	private BCryptPasswordEncoder encoder;

	/**
	 * 查询全部列表
	 * @return
	 */
	public List<User> findAll() {
		return userDao.findAll();
	}

	
	/**
	 * 条件查询+分页
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<User> findSearch(Map whereMap, int page, int size) {
		Specification<User> specification = createSpecification(whereMap);
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return userDao.findAll(specification, pageRequest);
	}

	
	/**
	 * 条件查询
	 * @param whereMap
	 * @return
	 */
	public List<User> findSearch(Map whereMap) {
		Specification<User> specification = createSpecification(whereMap);
		return userDao.findAll(specification);
	}

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public User findById(String id) {
		return userDao.findById(id).get();
	}

	/**
	 * 增加
	 * @param user
	 * @param code
	 */
	public void add(User user, String code) {
		//判断验证码是否为空
		String smscode = (String) redisTemplate.opsForValue().get("smscode_" + user.getMobile());
		if (StringUtils.isBlank(smscode)){
			new RuntimeException("请点击获取短信验证码");
		}
		if (!smscode.equals(code)){
			new RuntimeException("验证码输入不正确");
		}
		String password = encoder.encode(user.getPassword());
		user.setPassword(password);
		user.setId( idWorker.nextId()+"" );
		user.setRegdate(new Date());
		user.setUpdatedate(user.getRegdate());
		user.setOnline(0L);
		user.setFanscount(0);
		user.setFanscount(0);
		user.setLastdate(user.getRegdate());
		userDao.save(user);
	}

	/**
	 * 修改
	 * @param user
	 */
	public void update(User user) {
		userDao.save(user);
	}

	/**
	 * 删除
	 * @param id
	 */
	public void deleteById(String id) {
		userDao.deleteById(id);
	}

	/**
	 * 动态条件构建
	 * @param searchMap
	 * @return
	 */
	private Specification<User> createSpecification(Map searchMap) {

		return new Specification<User>() {

			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
                // ID
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                	predicateList.add(cb.like(root.get("id").as(String.class), "%"+(String)searchMap.get("id")+"%"));
                }
                // 手机号码
                if (searchMap.get("mobile")!=null && !"".equals(searchMap.get("mobile"))) {
                	predicateList.add(cb.like(root.get("mobile").as(String.class), "%"+(String)searchMap.get("mobile")+"%"));
                }
                // 密码
                if (searchMap.get("password")!=null && !"".equals(searchMap.get("password"))) {
                	predicateList.add(cb.like(root.get("password").as(String.class), "%"+(String)searchMap.get("password")+"%"));
                }
                // 昵称
                if (searchMap.get("nickname")!=null && !"".equals(searchMap.get("nickname"))) {
                	predicateList.add(cb.like(root.get("nickname").as(String.class), "%"+(String)searchMap.get("nickname")+"%"));
                }
                // 性别
                if (searchMap.get("sex")!=null && !"".equals(searchMap.get("sex"))) {
                	predicateList.add(cb.like(root.get("sex").as(String.class), "%"+(String)searchMap.get("sex")+"%"));
                }
                // 头像
                if (searchMap.get("avatar")!=null && !"".equals(searchMap.get("avatar"))) {
                	predicateList.add(cb.like(root.get("avatar").as(String.class), "%"+(String)searchMap.get("avatar")+"%"));
                }
                // E-Mail
                if (searchMap.get("email")!=null && !"".equals(searchMap.get("email"))) {
                	predicateList.add(cb.like(root.get("email").as(String.class), "%"+(String)searchMap.get("email")+"%"));
                }
                // 兴趣
                if (searchMap.get("interest")!=null && !"".equals(searchMap.get("interest"))) {
                	predicateList.add(cb.like(root.get("interest").as(String.class), "%"+(String)searchMap.get("interest")+"%"));
                }
                // 个性
                if (searchMap.get("personality")!=null && !"".equals(searchMap.get("personality"))) {
                	predicateList.add(cb.like(root.get("personality").as(String.class), "%"+(String)searchMap.get("personality")+"%"));
                }
				
				return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

			}
		};

	}

	/**
	 * 发送手机验证码
	 * @param mobile
	 */
    public void sendsms(String mobile) {
		//1.生成6位验证码
		int min = 100000;
		int max = 999999;
		Random random = new Random();
		int code = random.nextInt(max);
		if(code < min){
			code += min;
		}
		System.out.println(mobile+"收到验证码是："+code);

		//把验证码存到redis中设置时间五分钟
		redisTemplate.opsForValue().set("smscode_"+mobile, code+"",5,TimeUnit.MINUTES);

		//把数据手机和验证码发送到RabbitMQ
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("mobile",mobile );
		map.put("code",code +"");
        rabbitTemplate.isConfirmListener();
		rabbitTemplate.convertAndSend("sms",map);
    }

	/**
	 * 登录
	 * @param mobile
	 * @param password
	 * @return
	 */
	public User findByMobileAndPassword(String mobile, String password) {
		User user = userDao.findByMobile(mobile);

		if (user != null&&encoder.matches(password,user.getPassword())) {
			return user;
		}else {
			return null;
		}
	}

	/**
	 * 更新粉丝数
	 * @param userId
	 * @param count
	 */
	@Transactional
    public void incFanscount(String userId, int count) {
		userDao.incFanscount(userId,count);
    }
	/**
	 * 更新关注数
	 * @param userId
	 * @param count
	 */
	@Transactional
	public void incFollowcount(String userId, int count) {
		userDao.incFollowcount(userId,count);

	}
}
