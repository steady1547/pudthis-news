package com.linebot.springboot.support;

import com.linebot.springboot.enumeration.PudGroup;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 오늘의 게릴라 정보를 가져온다.
 * 퍼드 ID에 따른 조별 게릴라만 가져올수 있음. 시작용게릴라같은건 처리 못함
 * 커뮤니티 사이트에서 긁어오는 정보이기 때문에 데이터 패턴이 틀려지면 수집불가함
 */
@Component
public class TodayGuerrilla {

    private static final Logger LOG = LoggerFactory.getLogger(TodayGuerrilla.class);

    private static final String[] GROUPS = PudGroup.getNames(PudGroup.class);
    private static final String PUD_INVEN_URL = "http://www.inven.co.kr/board/pad/3944/";
    private static final int START_BOARD_NUMBER = 2338;
    private static final String START_DATE = "2017-11-14";

    private String[] titles;

    public List<Guerrilla> getTodayGuerrillaInfo() {
        List<Guerrilla> guerrillas = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(PUD_INVEN_URL + getToDayBoardNumber()).get();
            Elements guerrillaData = doc.select("table[class=__se_tbl]");
            if (guerrillaData.text().indexOf(GROUPS[0]) > -1) {
                LOG.debug("guerrillaData: {}", guerrillaData.text());
                int titleCount = getTitleCount(doc);
                if (titleCount > 1) {
                    titles = new String[titleCount];
                    for (int i = 0; i < titles.length; i++) {
                        titles[i] = getTitleData(doc, i);
                    }
                    addGuerrilla(guerrillaData.text(), 0, guerrillas);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return guerrillas;
    }

    private String getTitleData(Document doc, int i) {
        return doc.select("table[class=__se_tbl]").select("tr:eq(0) > td").eq(i + 1).text().trim();
    }

    private int getTitleCount(Document doc) {
        return doc.select("table[class=__se_tbl]").select("tr:eq(0) > td").size() - 1;
    }

    private int getToDayBoardNumber() {
        long diffDays = 0;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date beginDate = formatter.parse(START_DATE);
            Date endDate = new Date();
            diffDays = (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return START_BOARD_NUMBER + (int) diffDays;
    }

    private void addGuerrilla(String text, int groupIndex, List<Guerrilla> guerrillas) {
        String[] times = getTimeList(text, groupIndex);
        for (int i = 0; i < titles.length; i++) {
            guerrillas.add(new Guerrilla(titles[i].trim(), GROUPS[groupIndex], times[i].trim()));
        }

        if (groupIndex + 1 < GROUPS.length) {
            addGuerrilla(text, groupIndex + 1, guerrillas);
        }
    }

    private String[] getTimeList(String text, int groupIndex) {
        if (groupIndex + 1 == GROUPS.length) {
            return text.substring(text.indexOf(GROUPS[groupIndex]) + 2).split(" ");
        } else {
            return text.substring(text.indexOf(GROUPS[groupIndex]) + 2, text.indexOf(GROUPS[groupIndex + 1])).split(" ");
        }
    }

    /**
     * The type Guerrilla.
     */
    public static class Guerrilla {
        private String title;
        private String group;
        private String time;

        /**
         * Instantiates a new Guerrilla.
         *
         * @param title the title
         * @param group the GROUPS
         * @param time  the time
         */
        public Guerrilla(String title, String group, String time) {
            this.title = title;
            this.group = group;
            this.time = time;
        }

        /**
         * Gets title.
         *
         * @return the title
         */
        public String getTitle() {
            return title;
        }

        /**
         * Gets GROUPS.
         *
         * @return the GROUPS
         */
        public String getGroup() {
            return group;
        }

        /**
         * Gets time.
         *
         * @return the time
         */
        public String getTime() {
            return time;
        }
    }
}
