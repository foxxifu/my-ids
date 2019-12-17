package com.interest.ids.biz.authorize.utils;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.interest.ids.common.project.bean.sm.ResourceM;
import com.interest.ids.common.project.bean.sm.UserInfo;
import com.interest.ids.commoninterface.service.sm.IResourceMService;
import com.interest.ids.redis.caches.SessionCache;

@Component
public class AccessUtils {

	private static final Logger log = LoggerFactory.getLogger(AccessUtils.class);

	@Resource
	private SessionCache sessionCache;

	@Resource
	private IResourceMService resourceMService;

	public Boolean access(String uri, String tokenId) {
		if (null != sessionCache && null != uri && null != tokenId) {

			UserInfo user = sessionCache.getAttribute(tokenId, "user");
			if (null != user) {
				if ("system".equals(user.getType_())) {
					return true;
				}
				List<ResourceM> resources = null;
				if (null != user.getResources()
						&& user.getResources().size() > 0) {
					resources = user.getResources();
				} else {
					resources = resourceMService
							.selectAllResourceMByUserId(user.getId());
					user.setResources(resources);
					sessionCache.setAttribute("user", tokenId, user);
				}
				for (ResourceM resource : resources) {
					if (uri.equals(resource.getAuthUrl())) {
						log.info("userId = " + user.getId() + " uri= " + uri
								+ "accessed");
						return true;
					}
				}
			} else {
				log.error(" uri= " + uri + "fail, maybe no user login");
				return false;
			}
		} else {
			log.error("access fail");
			return false;
		}
		log.error("access fail");
		return false;
	}
}
