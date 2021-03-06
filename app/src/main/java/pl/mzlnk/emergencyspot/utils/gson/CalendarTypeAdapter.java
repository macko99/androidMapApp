package pl.mzlnk.emergencyspot.utils.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import lombok.SneakyThrows;

public class CalendarTypeAdapter extends TypeAdapter<Calendar> implements JsonSerializer<Calendar>, JsonDeserializer<Calendar> {

    private static final Gson gson = new GsonBuilder().create();
    private static final TypeAdapter<Date> dateTypeAdapter = gson.getAdapter(Date.class);
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public JsonElement serialize(Calendar src, Type type, JsonSerializationContext context) {
        if (src == null) {
            return null;
        } else {
            JsonObject jo = new JsonObject();
            jo.addProperty("$date", formatter.format(src.getTime()));
            return jo;
        }
    }

    @Override
    @SneakyThrows
    public Calendar deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        Date date = formatter.parse(json.getAsJsonObject().get("$date").getAsString());

        Calendar result = Calendar.getInstance();
        result.setTime(date);

        return result;
    }

    @Override
    public void write(JsonWriter out, Calendar value) throws IOException {
        dateTypeAdapter.write(out, value.getTime());
    }

    @Override
    public Calendar read(JsonReader in) throws IOException {
        Date read = dateTypeAdapter.read(in);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(read);

        return calendar;
    }

}
