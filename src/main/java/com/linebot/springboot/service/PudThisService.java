package com.linebot.springboot.service;

/**
 * The interface Pud this service.
 */
public interface PudThisService {
    /**
     * Update news boolean.
     *
     * @return the boolean
     */
    boolean updateNews();

    /**
     * Update news list.
     */
    void updateNewsList();

    /**
     * Gets news.
     *
     * @return the news
     */
    String getNews();

    /**
     * Find monster string.
     *
     * @param keyword the keyword
     * @return the string
     */
    String findMonster(String keyword);

    /**
     * Find monster by num string.
     *
     * @param num the num
     * @return the string
     */
    String findMonsterByNum(int num);

    /**
     * 입력받은 시간의 게릴라 던전 정보를 유저에게 push
     *
     * @param nowTime the now time
     */
    void pushGuerrillaInfo(String nowTime);
}
