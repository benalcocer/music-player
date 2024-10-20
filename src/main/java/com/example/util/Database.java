package com.example.util;

import com.example.models.Playlist;
import com.example.models.Song;
import com.example.models.User;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import kong.unirest.JsonNode;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

public class Database {

    private final String host;

    private String token = "";

    public Database(String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    // Auth
    public boolean doesUserExist(String email) {
        Map<String, Object> jsonBodyMap = Map.of(
            "email", email
        );
        try {
            String output = BackendConnection.postStringRequest(host + "/" + RequestMapping.AUTH.getMapping() + "/" + "exists", JSONUtils.convertToJSON(jsonBodyMap));
            return output != null;
        } catch (Exception e) {
            Logger.getGlobal().warning(e.getMessage());
            return false;
        }
    }

    public boolean isUserEnabled(String email) {
        Map<String, Object> jsonBodyMap = Map.of(
            "email", email
        );
        try {
            String output = BackendConnection.postStringRequest(host + "/" + RequestMapping.AUTH.getMapping() + "/" + "enabled", JSONUtils.convertToJSON(jsonBodyMap));
            return output != null;
        } catch (Exception e) {
            Logger.getGlobal().warning(e.getMessage());
            return false;
        }
    }

    public boolean userSignup(String email, String password) {
        Map<String, Object> jsonBodyMap = Map.of(
            "email", email,
            "password", password
        );
        try {
            JsonNode jsonNode = BackendConnection.postJsonNodeRequest(host + "/" + RequestMapping.AUTH.getMapping() + "/" + "signup", JSONUtils.convertToJSON(jsonBodyMap));
            return jsonNode != null && !jsonNode.isArray();
        } catch (Exception e) {
            Logger.getGlobal().warning(e.getMessage());
            return false;
        }
    }

    public JSONObject login(String email, String password) {
        Map<String, Object> jsonBodyMap = Map.of(
            "email", email,
            "password", password
        );
        try {
            JsonNode jsonNode = BackendConnection.postJsonNodeRequest(host + "/" + RequestMapping.AUTH.getMapping() + "/" + "login", JSONUtils.convertToJSON(jsonBodyMap));
            if (jsonNode != null && !jsonNode.isArray()) {
                return jsonNode.getObject();
            }
        } catch (Exception e) {
            Logger.getGlobal().warning(e.getMessage());
        }
        return null;
    }

    public boolean verifyUser(String email, String verificationCode) {
        Map<String, Object> jsonBodyMap = Map.of(
            "email", email,
            "verificationCode", verificationCode
        );
        try {
            String response = BackendConnection.postStringRequest(host + "/" + RequestMapping.AUTH.getMapping() + "/" + "verify", JSONUtils.convertToJSON(jsonBodyMap));
            return response != null;
        } catch (Exception e) {
            Logger.getGlobal().warning(e.getMessage());
        }
        return false;
    }

    public boolean resendCode(String email) {
        Map<String, Object> jsonBodyMap = Map.of(
            "email", email
        );
        try {
            String response = BackendConnection.postStringRequest(host + "/" + RequestMapping.AUTH.getMapping() + "/" + "resend", JSONUtils.convertToJSON(jsonBodyMap));
            return response != null;
        } catch (Exception e) {
            Logger.getGlobal().warning(e.getMessage());
        }
        return false;
    }

    // Playlists
    public Playlist savePlaylist(Playlist playlist) throws Exception {
        JsonNode jsonNode = BackendConnection.postJsonNodeRequest(host + "/" + RequestMapping.PLAYLISTS.getMapping() + "/save", JSONUtils.convertToJSON(playlist), getToken());
        if (jsonNode != null && !jsonNode.isArray()) {
            return JSONUtils.convertToObject(Playlist.class, jsonNode.getObject().toString());
        }
        return null;
    }

    // Users
    public User updateUser(User user) throws Exception {
        JsonNode jsonNode = BackendConnection.postJsonNodeRequest(host + "/" + RequestMapping.USERS.getMapping() + "/update", JSONUtils.convertToJSON(user), getToken());
        if (jsonNode != null && !jsonNode.isArray()) {
            return JSONUtils.convertToObject(User.class, jsonNode.getObject().toString());
        }
        return null;
    }

    // Songs
    public List<Song> getPlaylistSongs(String playlistId) {
        String searchId = URLEncoder.encode(playlistId, StandardCharsets.UTF_8);
        return getList(Song.class, host + "/" + RequestMapping.SONGS.getMapping() + "/from-playlist/" + searchId, true);
    }

    public boolean saveSong(Song song) {
        try {
            JsonNode jsonNode = BackendConnection.postJsonNodeRequest(host + "/" + RequestMapping.SONGS.getMapping() + "/save", JSONUtils.convertToJSON(song), getToken());
            return jsonNode != null && !jsonNode.isArray();
        } catch (Exception e) {
            return false;
        }
    }

    // Playlists
    public List<Playlist> getFromUser(User user) {
        String searchId = URLEncoder.encode(user.getId(), StandardCharsets.UTF_8);
        return getList(Playlist.class, host + "/" + RequestMapping.PLAYLISTS.getMapping() + "/from-user/" + searchId, true);
    }

    public boolean deletePlaylist(String playlistId) {
        return BackendConnection.deleteRequest(host + "/" + RequestMapping.PLAYLISTS.getMapping() + "/" + playlistId, getToken());
    }

    // Storage
    public String getSignedURI(String songId) {
        try {
            return BackendConnection.getStringRequest(host + "/" + RequestMapping.STORAGE.getMapping() + "/" + songId, getToken());
        } catch (Exception e) {
            return null;
        }
    }

    public List<Song> searchSongs(String searchText) {
        String searchId = URLEncoder.encode(searchText, StandardCharsets.UTF_8);
        return getList(Song.class, host + "/" + RequestMapping.SONGS.getMapping() + "/search/" + searchId, true);
    }

    /**
     * Perform a get request to retrieve a list of objects.
     *
     * @param tClass The Class type for the elements in the list.
     * @param request The request to perform.
     * @param authed If true then send an authed request, otherwise send without any auth.
     * @return A list containing elements from the Get request.
     */
    private <T> List<T> getList(Class<T> tClass, String request, boolean authed) {
        ArrayList<T> elements = new ArrayList<>();
        try {
            JsonNode jsonNode = BackendConnection.getJsonNodeRequest(request, authed ? getToken() : "");
            if (jsonNode != null && jsonNode.isArray()) {
                JSONArray jsonArray = jsonNode.getArray();
                for (int i = 0; i < jsonArray.length(); i++) {
                    T element = JSONUtils.convertToObject(tClass, jsonArray.getJSONObject(i).toString());
                    if (element != null) {
                        elements.add(element);
                    }
                }
            }
        } catch (Exception e) {
            Logger.getGlobal().info("Failed to get request a list.");
            elements.clear();
        }
        return elements;
    }

    private enum RequestMapping {
        PLAYLISTS,
        SONGS,
        STORAGE,
        AUTH,
        USERS;

        private String mapping = "";

        static {
            PLAYLISTS.mapping = "api/v1/playlists";
            SONGS.mapping = "api/v1/songs";
            STORAGE.mapping = "api/v1/storage";
            AUTH.mapping = "auth";
            USERS.mapping = "api/v1/users";
        }

        public String getMapping() {
            return mapping;
        }
    }
}
