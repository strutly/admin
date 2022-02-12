package com.tsingtec.admin.config.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

import javax.servlet.annotation.WebListener;
import java.util.concurrent.atomic.AtomicInteger;

@WebListener
public class ShiroSessionListener implements SessionListener {

    /**
     * 统计在线人数
     * juc包下线程安全自增
     */
    private final AtomicInteger sessionCount = new AtomicInteger(0);

    /**
     * 会话创建时触发
     * @param session
     */
    @Override
    public void onStart(Session session) {
        //会话创建，在线人数加一
        sessionCount.incrementAndGet();
        System.out.println("在线人数登录:"+sessionCount.get());
    }

    /**
     * 退出会话时触发
     * @param session
     */
    @Override
    public void onStop(Session session) {
        //会话退出,在线人数减一
        sessionCount.decrementAndGet();
        System.out.println("在线人数:"+sessionCount.get());
    }

    /**
     * 会话过期时触发
     * @param session
     */
    @Override
    public void onExpiration(Session session) {
        //会话过期,在线人数减一
        sessionCount.decrementAndGet();
        System.out.println("在线人数过期:"+sessionCount.get());
    }
    /**
     * 获取在线人数使用
     * @return
     */
    public AtomicInteger getSessionCount() {
        return sessionCount;
    }
}