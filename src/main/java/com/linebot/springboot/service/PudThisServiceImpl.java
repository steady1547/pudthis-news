package com.linebot.springboot.service;

import com.linebot.springboot.enumeration.PudGroup;
import com.linebot.springboot.instance.MessageString;
import com.linebot.springboot.instance.News;
import com.linebot.springboot.model.PudMessage;
import com.linebot.springboot.model.UserInfo;
import com.linebot.springboot.repository.PudMessageRepository;
import com.linebot.springboot.repository.UserInfoRepository;
import com.linebot.springboot.support.Push;
import com.linebot.springboot.support.ShortURL;
import com.linebot.springboot.support.TodayGuerrilla;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


/**
 * The type Pud this service.
 */
@Service
public class PudThisServiceImpl implements PudThisService {
    /**
     * The Log.
     */
    static final Logger LOG = LoggerFactory.getLogger(PudThisServiceImpl.class);

    private final static String BASE_URL = "http://www.thisisgame.com";
    private final static String CONTENT_PATH = "/pad/tboard/?board=21";
    private final static String CONTENT_URL = BASE_URL + CONTENT_PATH;
    private final static String SEARCH_MONSTER_PATH = "/pad/info/monster/list.php?sname=";
    private final static String SEARCH_MONSTER_NUM_PATH = "/pad/info/monster/list.php?numkr=";
    private final static String SEARCH_MONSTER_URL = BASE_URL + SEARCH_MONSTER_PATH;
    private final static String SEARCH_MONSTER_NUM_URL = BASE_URL + SEARCH_MONSTER_NUM_PATH;


    private final static String CLSS_LIST = "subject";
    private final static String CLSS_NOTICE = "notice-mark";

    private final static Integer GET_COUNT = 5;
    private final static Integer SEARCH_MONSTER_COUNT = 5;
    /**
     * The Pud message repository.
     */
    @Autowired
    PudMessageRepository pudMessageRepository;

    /**
     * The User info repository.
     */
    @Autowired
    UserInfoRepository userInfoRepository;

    /**
     * The Short url.
     */
    @Autowired
    ShortURL shortUrl;

    /**
     * The Push.
     */
    @Autowired
    Push push;

    @Autowired
    private TodayGuerrilla todayGuerrillaInfo;

    @Override
    @Deprecated
    public boolean updateNews() {
        Map<String, String> feed = new HashMap<String, String>();

        try {
            Document doc = this.getDocument(CONTENT_URL);
            Elements els = doc.getElementsByClass(CLSS_LIST);
            for (Element e : els) {
                if (isMaxCount(GET_COUNT, feed.size())) break;
                if (isNotice(e) || isJapanNews(e)) continue;

                Elements v = e.getElementsByTag("a");
                if (!v.isEmpty()) {
                    feed.put(getContentId(e), "* " + v.text() + "\n" + shortUrl.getShortURL(BASE_URL + v.attr("href")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this.update(feed);
    }

    private boolean isMaxCount(int maxCount, int size) {
        return size == maxCount ? true : false;
    }

    private boolean isNotice(Element e) {
        return (!e.getElementsByClass(CLSS_NOTICE).isEmpty());
    }

    private boolean isJapanNews(Element e) {
        return e.getElementsByClass("category").text().equals("일본소식");
    }

    private String getContentId(Element e) {
        return e.getElementsByTag("input").val();
    }

    private Document getDocument(String url) {
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return doc;
    }

    @Deprecated
    private boolean update(final Map<String, String> map) {
        News m = News.getInstance();
        LOG.debug("[PudThisServiceImpl.update] m.get().isEmpty() {} ", m.get().isEmpty());
        if (m.get().isEmpty()) {
            m.adjust(map);
            return false;
        }
        Map<String, String> old = m.get();

        for (Entry<String, String> entry : map.entrySet()) {
            if (!old.containsKey(entry.getKey())) {
                LOG.debug("new Entry Key : {} ", entry.getKey());
                m.adjust(map);
                return true;
            }
        }
        return false;
    }

    @Override
    public String findMonster(String keyword) {
        StringBuilder sb = new StringBuilder();
        List<String> list = new ArrayList<>();
        boolean isOverList = false;
        try {
            String url = SEARCH_MONSTER_URL + java.net.URLEncoder.encode(keyword, "UTF-8");
            Document doc = getDocument(url);
            Elements els = doc.getElementsByClass("left");
            for (Element e : els) {
                if (isMaxCount(SEARCH_MONSTER_COUNT, list.size())) {
                    isOverList = true;
                    break;
                }
                Elements name = e.getElementsByClass("name");
                for (Element ne : name) {
                    Elements tag = ne.getElementsByTag("a");
                    if (!tag.isEmpty()) {
                        add(list, tag);
                    }
                }
            }

            if (list.size() > 0) {
                for (String data : list) {
                    sb.append(data).append("\n");
                }
                if (isOverList) {
                    sb.append(MessageString.replaceString(MessageString.MORE_RESULT, shortUrl.getShortURL(url)));
                }
            } else {
                sb.append(MessageString.NO_RESULT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private void add(List<String> list, Elements tag) {

        if (!tag.isEmpty()) {
            list.add(new StringBuilder("- ")
                    .append(tag.text()).append("\n")
                    .append(shortUrl.getShortURL(tag.attr("href"))).toString());
        }
    }

    @Override
    public String findMonsterByNum(int num) {
        StringBuilder sb = new StringBuilder();
        List<String> list = new ArrayList<>();
        try {
            String url = SEARCH_MONSTER_NUM_URL + String.valueOf(num);
            Document doc = getDocument(url);
            Elements els = doc.getElementsByClass("left");
            for (Element e : els) {
                if (isMaxCount(SEARCH_MONSTER_COUNT, list.size())) break;
                Elements name = e.getElementsByClass("name");
                for (Element ne : name) {
                    Elements tag = ne.getElementsByTag("a");
                    if (!tag.isEmpty()) {
                        add(list, tag);
                    }
                }
            }

            if (list.size() > 0) {
                for (String data : list) {
                    sb.append(data).append("\n");
                }
            } else {
                sb.append(MessageString.NO_RESULT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    @Override
    public void updateNewsList() {
        List<PudMessage> list = getPudNewsList();
        List<PudMessage> newList = new ArrayList<>();
        for (PudMessage pudMsg : list) {
            if (!pudMessageRepository.existsByNo(pudMsg.getNo())) {
                pudMsg.setShorUrl(shortUrl.getShortURL(pudMsg.getLongUrl()));
                pudMessageRepository.insert(pudMsg);
                LOG.debug("INSERT NEWS AND PUSH ", pudMsg);
                newList.add(pudMsg);
            }
        }

        this.pushNewList(newList);

    }

    private void pushNewList(List<PudMessage> newList) {
        if (!newList.isEmpty()) {
            String pushMessage = MessageString.generateMessage(MessageString.NEW_INFO, newList);
            List<UserInfo> userList = userInfoRepository.findAll();
            for (UserInfo info : userList) {
                push.push(info.getUserId(), pushMessage);
            }
        }
    }

    private List<PudMessage> getNewsList() {
        int limit = 5;
        Pageable page = new PageRequest(0, limit, new Sort(Direction.DESC, "time"));
        return pudMessageRepository.findAll(page).getContent();
    }

    /**
     * Get pud news list list.
     *
     * @return the list
     */
    public List<PudMessage> getPudNewsList() {
        List<PudMessage> list = new ArrayList<PudMessage>();
        try {
            Document doc = this.getDocument(CONTENT_URL);
            Elements els = doc.getElementsByClass(CLSS_LIST);
            for (Element e : els) {
                if (isMaxCount(GET_COUNT, list.size())) break;
                if (isNotice(e) || isJapanNews(e)) continue;

                Elements v = e.getElementsByTag("a");
                if (!v.isEmpty()) {
                    list.add(new PudMessage(getContentId(e), v.text(), "", BASE_URL + v.attr("href"), System.currentTimeMillis()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public String getNews() {
        System.out.println(getNewsList());
        return MessageString.generateMessage(getNewsList());
    }

    @Override
    public void pushGuerrillaInfo(String nowTime) {
        List<UserInfo> userList = userInfoRepository.findAll();
        List<TodayGuerrilla.Guerrilla> guerrillaList = todayGuerrillaInfo.getTodayGuerrillaInfo();

        for (UserInfo info : userList) {
            if (StringUtils.isNotEmpty(info.getPudId()) && info.getPudId().length() == 9) {
                for (TodayGuerrilla.Guerrilla guerrilla : guerrillaList) {
                    if (isPushYn(info, guerrilla, nowTime)) {
                        push.push(info.getUserId(), MessageString.guerrillaGenerateMessage(guerrilla.getTitle()));
                    }
                }
            }
        }
    }

    private boolean isPushYn(UserInfo info, TodayGuerrilla.Guerrilla guerrilla, String nowTime) {
        return guerrilla.getGroup().equals(PudGroup.getGroup(info.getPudId())) && guerrilla.getTime().substring(0, guerrilla.getTime().length() - 1).equals(nowTime.substring(0, nowTime.length() - 1));
    }
}
