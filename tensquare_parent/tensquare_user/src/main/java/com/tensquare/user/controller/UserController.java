package com.tensquare.user.controller;

import com.tensquare.user.pojo.User;
import com.tensquare.user.service.UserService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
/**
 * 控制器层
 * @author Administrator
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private JwtUtil jwtUtil;


	/**
	 * 查询全部数据
	 * @return
	 */
	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true,StatusCode.OK,"查询成功",userService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",userService.findById(id));
	}


	/**
	 * 分页+多条件查询
	 * @param searchMap 查询条件封装
	 * @param page 页码
	 * @param size 页大小
	 * @return 分页结果
	 */
	@RequestMapping(value="/search/{page}/{size}",method=RequestMethod.POST)
	public Result findSearch(@RequestBody Map searchMap , @PathVariable int page, @PathVariable int size){
		Page<User> pageList = userService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<User>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",userService.findSearch(searchMap));
    }
	
	/**
	 * 注册用户
	 * @param user
	 */
	@RequestMapping(value = "/register/{code}",method=RequestMethod.POST)
	public Result add(@RequestBody User user ,@PathVariable String code ){

		userService.add(user,code);
		return new Result(true,StatusCode.OK,"注册成功");
	}
	
	/**
	 * 修改
	 * @param user
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody User user, @PathVariable String id ){
		user.setId(id);
		userService.update(user);		
		return new Result(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除用户
	 * @param id
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id , HttpServletRequest request){
		/*String authorization= request.getHeader("Authorization");使用拦截器
		if (authorization == null) {
			return new Result(false,StatusCode.ACCESSERROR,"权限不足");
		}
		if (!authorization.startsWith("Bearer ")) {
			return new Result(false,StatusCode.ACCESSERROR,"权限不足");
		}
		String token = authorization.substring(7);
		Claims claims = jwtUtil.parseJWT(token);

		if(claims==null){
			return new Result(false,StatusCode.ACCESSERROR,"权限不足");
		}
		if (!"admin".equals(claims.get("roles"))) {
			return new Result(false,StatusCode.ACCESSERROR,"权限不足");
		}*/
		Claims  claims = (Claims) request.getAttribute("admin_claims");
		if (claims==null){
			return new Result(true,StatusCode.ACCESSERROR,"无权访问");
		}
		userService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}

	/**
	 * 发送手机验证码
	 * @param mobile
	 * @return
	 */
	@RequestMapping(value = "/sendsms/{mobile}",method = RequestMethod.POST)
	public Result sendsms(@PathVariable String mobile) {
		userService.sendsms(mobile);
		return new Result(true,StatusCode.OK,"发送成功");
	}

	/**
	 * 用户登录
	 * @return
	 */
	@RequestMapping(value = "/login",method = RequestMethod.POST)
	public Result login(@RequestBody Map<String, String> map) {
		User user= userService.findByMobileAndPassword(map.get("mobile"),map.get("password"));
		if (user == null) {
			return new Result(false,StatusCode.LOGINERROR,"登录失败");
		}else {
			//签发token
			String token = jwtUtil.createJWT(user.getId(), user.getMobile(), "user");
			
			Map resultMap = new HashMap();
			resultMap.put("token", token);
			resultMap.put("name",user.getNickname() );
			resultMap.put("avatar",user.getAvatar() );
			return new Result(true,StatusCode.OK,"登录成功",resultMap);
		}
	}

	/**
	 * 更新粉丝数
	 * @param userId /incFans/{userId}/{count}
	 * @param count
	 * @return
	 */
	@RequestMapping(value = "/incFans/{userId}/{count}",method = RequestMethod.PUT)
	public Result incFanscount(@PathVariable String userId,@PathVariable int count) {

		userService.incFanscount(userId,count);
		return new Result(true,StatusCode.OK,"更新成功");
	}

	/**
	 * 更新关注数
	 * @param userId /incFollowcount/{userId}/{count}
	 * @param count
	 * @return
	 */
	@RequestMapping(value = "/incFollow/{userId}/{count}",method = RequestMethod.PUT)
	public Result incFollowcount(@PathVariable String userId,@PathVariable int count) {

		userService.incFollowcount(userId,count);
		return new Result(true,StatusCode.OK,"更新成功");
	}

	
}
