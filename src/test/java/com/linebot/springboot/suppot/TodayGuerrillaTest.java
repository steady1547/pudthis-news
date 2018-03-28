package com.linebot.springboot.suppot;

import com.linebot.springboot.instance.MessageString;
import com.linebot.springboot.model.UserInfo;
import com.linebot.springboot.repository.UserInfoRepository;
import com.linebot.springboot.service.PudThisServiceImpl;
import com.linebot.springboot.support.Push;
import com.linebot.springboot.support.TodayGuerrilla;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by line play on 2017-11-08.
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class TodayGuerrillaTest {

    @InjectMocks
    private PudThisServiceImpl pudThisService;

    @Mock
    private UserInfoRepository userInfoRepository;

    @Mock
    private TodayGuerrilla todayGuerrillaInfo;

    @Mock
    private Push push;

    private static final String USER_ID = "111111111111";
    private static final String PUD_ID = "234567891";
    private static final String GROUP = "E";
    private static final String TIME = "18:10";
    private static final String TITLE = "레이더드래곤";

    @Before
    public void setUp() throws Exception {
        pudThisService = new PudThisServiceImpl();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testTitleExtract() throws Exception {
        String targetData = "TEST";
        String html = "<table><tr><td>1</td><td>" + targetData + "</td></tr><tr><td>3</td><td>4</td></tr></table>";
        Document doc = Jsoup.parse(html);
        Elements ele = doc.select("table").select("tr:eq(0) > td");

        Assert.assertEquals(ele.size(), 2);
        Assert.assertEquals(targetData, ele.eq(1).text());
    }

    @Test
    public void testGetNowGuerrilla() throws Exception {
        when(userInfoRepository.findAll()).thenReturn(getUserInfos());
        when(todayGuerrillaInfo.getTodayGuerrillaInfo()).thenReturn(new ArrayList<>(Arrays.asList(new TodayGuerrilla.Guerrilla(TITLE, GROUP, TIME))));

        pudThisService.pushGuerrillaInfo(TIME);

        verify(push).push(USER_ID, MessageString.guerrillaGenerateMessage(TITLE));
    }

    private List<UserInfo> getUserInfos() {
        List<UserInfo> userList = new ArrayList<>();
        UserInfo userInfo = new UserInfo();
        userInfo.setPudId(PUD_ID);
        userInfo.setUserId(USER_ID);
        userList.add(userInfo);
        return userList;
    }
}
