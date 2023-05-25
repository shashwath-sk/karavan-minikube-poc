package notes;

import org.apache.camel.BindToRegistry;
import javax.inject.Named;
import java.util.*;
import org.apache.camel.Exchange;
import com.google.gson.Gson;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.*;
import java.lang.*;

@Named("notesBean")
@BindToRegistry("notesBean")
public class Notes {
    private static final String[] fields = { "title", "category", "body" };

    public boolean checkFields(HashMap<String, Object> note) {
        for (String field : fields) {
            if (!note.containsKey(field)) {
                return false;
            }
        }
        return true;
    }

    public void setQueryParams(Exchange exchange) {
        String queryString = exchange.getIn().getHeader(Exchange.HTTP_QUERY).toString();
        String[] queryArray = queryString.split("&");
        HashMap<String, Object> queryMap = new HashMap<String, Object>();

        if (queryString.length() == 0) {
            return;
        }
        for (String query : queryArray) {
            String[] queryPair = query.split("=");
            queryMap.put(queryPair[0], queryPair[1]);
        }
        if (queryMap.containsKey("title")) {
            exchange.setProperty("title", queryMap.get("title"));
        }
        if (queryMap.containsKey("category")) {
            exchange.setProperty("category", queryMap.get("category"));
        }
        System.out.println(queryMap);
    }

    public void setNoteBody(Exchange exchange) throws Exception {
        String body = exchange.getIn().getBody(String.class);
        System.out.println(body);
        HashMap<String, Object> note = new Gson().fromJson(body, HashMap.class);
        if (this.checkFields(note)) {
            for (String field : fields) {
                exchange.setProperty(field, note.get(field));
            }
        } else {
            throw new Exception("Required fields are missing");
        }
    }

    public HashMap<String, Object> createNote(Exchange exchange) throws Exception {
        HashMap<String, Object> newNote = new HashMap<String, Object>();
        for (String field : fields) {
            newNote.put(field, exchange.getProperty(field));
        }
        newNote.put("userId", exchange.getProperty("userId"));
        System.out.println(newNote);

        return newNote;
    }

    public HashMap<String, Object> getAllNotes(Exchange exchange) {
        HashMap<String, Object> queryMap = new HashMap<String, Object>();
        if (exchange.getProperty("title") != null) {
            queryMap.put("title", exchange.getProperty("title"));
        }
        if (exchange.getProperty("category") != null) {
            queryMap.put("category", exchange.getProperty("category"));
        }
        System.out.println(queryMap);
        return queryMap;
    }

    public HashMap<String, Object> updateWholeNote(Exchange exchange) {
        HashMap<String, Object> newNote = new HashMap<String, Object>();
        for (String field : fields) {
            exchange.setProperty(field, newNote.get(field));
        }
        return newNote;
    }

    public void updatePartialNote(Exchange exchange) throws Exception {
        String path = exchange.getIn().getHeader(Exchange.HTTP_PATH).toString();
        String[] pathArray = path.split("/");
        String id = pathArray[pathArray.length - 1];
        String body = exchange.getIn().getBody(String.class);
        HashMap<String, Object> note = new Gson().fromJson(body, HashMap.class);
        HashMap<String, Object> newNote = new HashMap<String, Object>();
        exchange.setProperty("id", id);
        for (String field : fields) {
            exchange.setProperty(field, null);
        }
        for (String key : note.keySet()) {
            if (Arrays.asList(fields).contains(key)) {
                exchange.setProperty(key, note.get(key));
            }
        }
    }

    public HashMap<String, Object> deleteNote(Exchange exchange) {
        String body = exchange.getIn().getBody(String.class);
        HashMap<String, Object> note = new Gson().fromJson(body, HashMap.class);
        System.out.println(note);
        return note;
    }

    public void setNoteId(Exchange exchange) {
        String path = exchange.getIn().getHeader(Exchange.HTTP_PATH).toString();
        String[] pathArray = path.split("/");
        String id = pathArray[pathArray.length - 1];
        exchange.setProperty("id", id);
    }

    public HashMap<String, Object> getNoteById(Exchange exchange) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("id", exchange.getProperty("id"));
        return map;
    }

    public HashMap<String, Object> deleteNoteById(Exchange exchange) {
        String path = exchange.getIn().getHeader(Exchange.HTTP_PATH).toString();
        String[] pathArray = path.split("/");
        String id = pathArray[pathArray.length - 1];
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        exchange.setProperty("id", id);
        System.out.println(map);
        return map;
    }

    public HashMap<String, Object> getNotesByTitle(Exchange exchange) {
        String query = exchange.getIn().getHeader(Exchange.HTTP_QUERY).toString();
        String[] queryArray = query.split("=");
        System.out.println(queryArray);
        String title = queryArray[queryArray.length - 1];
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("title", title);
        System.out.println(map);
        return map;
    }

    public HashMap<String, Object> sendErrorResponse(Exchange exchange) {
        String error = exchange.getProperty(Exchange.EXCEPTION_CAUGHT).toString();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("error", error);
        return map;
    }

    public HashMap<String, Object> sendMessage(Exchange exchange) {
        String headerMessage = exchange.getIn().getHeader("message").toString();
        int responseCode = Integer.parseInt(exchange.getIn().getHeader("responseCode").toString());
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, responseCode);
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("message", headerMessage);
        return map;
    }

    public boolean checkTitleInBody(Exchange exchange) {
        String body = exchange.getIn().getBody(String.class);
        HashMap<String, Object> note = new Gson().fromJson(body, HashMap.class);
        if (note.containsKey("title")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkCategoryInBody(Exchange exchange) {
        String body = exchange.getIn().getBody(String.class);
        HashMap<String, Object> note = new Gson().fromJson(body, HashMap.class);
        if (note.containsKey("category")) {
            return true;
        } else {
            return false;
        }
    }

    // public HashMap<String, Object> setNoteId(Exchange exchange) {
    // HashMap<String, Object> note = new HashMap<String, Object>();
    // String id = exchange.getIn().getHeader("id").toString();
    // note.put("id", id);
    // return note;
    // }
}
