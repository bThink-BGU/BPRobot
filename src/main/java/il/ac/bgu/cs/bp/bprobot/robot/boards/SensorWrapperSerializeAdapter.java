package il.ac.bgu.cs.bp.bprobot.robot.boards;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class SensorWrapperSerializeAdapter implements JsonSerializer<SensorWrapper<Object>> {
  @Override
  public JsonElement serialize(SensorWrapper<Object> sensorWrapper, Type type, JsonSerializationContext jsonSerializationContext) {
    var obj = new JsonObject();
    obj.addProperty("name", sensorWrapper.name);
    obj.addProperty("port", sensorWrapper.port.getName());
    return obj;
  }
}
