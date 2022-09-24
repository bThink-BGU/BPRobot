package il.ac.bgu.cs.bp.bprobot.robot.boards;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Map;

public class SensorWrapperSerializeAdapter implements JsonSerializer<Map.Entry<SensorWrapper<?>,float[]>> {
  @Override
  public JsonElement serialize(Map.Entry<SensorWrapper<?>, float[]> sensorWrapperEntry, Type type, JsonSerializationContext jsonSerializationContext) {
    var obj = new JsonObject();
    obj.addProperty("name", sensorWrapperEntry.getKey().name);
    obj.addProperty("port", sensorWrapperEntry.getKey().getName());
    var arr = new JsonArray();
    for (var f : sensorWrapperEntry.getValue()) {
      arr.add(new JsonPrimitive(f));
    }
    obj.add("data", arr);
    return obj;
  }
}
