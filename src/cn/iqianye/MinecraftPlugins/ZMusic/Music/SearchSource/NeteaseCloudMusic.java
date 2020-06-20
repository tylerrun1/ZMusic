package cn.iqianye.MinecraftPlugins.ZMusic.Music.SearchSource;

import cn.iqianye.MinecraftPlugins.ZMusic.Utils.NetUtils;
import com.google.gson.*;

import java.net.URLEncoder;

public class NeteaseCloudMusic {

    public NeteaseCloudMusic() {

    }

    /**
     * 获取音乐链接
     *
     * @param musicName 音乐名称
     * @return 音乐链接
     */
    public static JsonObject getMusicUrl(String musicName) {
        try {
            String getUrl =
                    "https://music.163.com/api/search/get/web?csrf_token=Referer="
                            +
                            "http://music.163.com/search/&hlposttag="
                            +
                            URLEncoder.encode("</span>&hlpretag=< span class=\"s-fc7\">", "utf-8")
                            +
                            "&limit=1&s="
                            +
                            URLEncoder.encode(musicName, "utf-8")
                            +
                            "&type=1";
            Gson gson = new Gson();
            String jsonText = NetUtils.getNetString(getUrl, null);
            JsonObject json = gson.fromJson(jsonText, JsonObject.class);
            JsonObject result = json.getAsJsonObject("result");
            if (result != null || result.get("songCount").getAsInt() != 0) {
                JsonObject jsonOut = result.getAsJsonArray("songs").get(0).getAsJsonObject();
                int musicID = jsonOut.get("id").getAsInt();
                String musicUrl = "http://music.163.com/song/media/outer/url?id=" + musicID + ".mp3";
                String lyricJsonText = NetUtils.getNetString("https://music.163.com/api/song/media?id=" + musicID, null);
                JsonObject lyricJson = gson.fromJson(lyricJsonText, JsonObject.class);
                String name = jsonOut.get("name").getAsString();
                int inttime = jsonOut.get("duration").getAsInt();
                inttime = inttime / 1000;
                String time = String.valueOf(inttime);
                JsonArray singer = jsonOut.get("artists").getAsJsonArray();
                String singerName = "";
                for (JsonElement j : singer) {
                    singerName += j.getAsJsonObject().get("name").getAsString() + "/";
                }
                singerName = singerName.substring(0, singerName.length() - 1);
                String lyric = "";
                try {
                    lyric = lyricJson.get("lyric").getAsString();
                    lyric = lyric.replaceAll("\r", "");
                } catch (Exception e) {

                }
                JsonObject returnJson = new JsonObject();
                returnJson.addProperty("id", musicID);
                returnJson.addProperty("url", musicUrl);
                returnJson.addProperty("time", time);
                returnJson.addProperty("name", name);
                returnJson.addProperty("singer", singerName);
                returnJson.addProperty("lyric", lyric);
                return returnJson;
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取音乐列表
     *
     * @param musicName 音乐名称
     * @return 音乐列表数组
     */
    public static JsonArray getMusicList(String musicName) {
        try {
            String getUrl =
                    "https://music.163.com/api/search/get/web?csrf_token=Referer="
                            +
                            "http://music.163.com/search/&hlposttag="
                            +
                            URLEncoder.encode("</span>&hlpretag=< span class=\"s-fc7\">", "utf-8")
                            +
                            "&limit=10&s="
                            +
                            URLEncoder.encode(musicName, "utf-8")
                            +
                            "&type=1";
            Gson gson = new GsonBuilder().create();
            String jsonText = NetUtils.getNetString(getUrl, null);
            JsonObject json = gson.fromJson(jsonText, JsonObject.class);
            JsonObject result = json.getAsJsonObject("result");
            JsonArray returnJson = new JsonArray();
            if (result != null || result.get("songCount").getAsInt() != 0) {
                JsonArray jsonOut = result.getAsJsonArray("songs");
                for (JsonElement j : jsonOut) {
                    String name = j.getAsJsonObject().get("name").getAsString();
                    int musicID = j.getAsJsonObject().get("id").getAsInt();
                    JsonArray singer = j.getAsJsonObject().get("artists").getAsJsonArray();
                    String singerName = "";
                    for (JsonElement js : singer) {
                        singerName += js.getAsJsonObject().get("name").getAsString() + "/";
                    }
                    singerName = singerName.substring(0, singerName.length() - 1);
                    JsonObject returnJsonObj = new JsonObject();
                    returnJsonObj.addProperty("id", musicID);
                    returnJsonObj.addProperty("name", name);
                    returnJsonObj.addProperty("singer", singerName);
                    returnJson.add(returnJsonObj);
                }
                return returnJson;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}



