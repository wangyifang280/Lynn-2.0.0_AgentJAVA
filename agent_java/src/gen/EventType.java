/**
 * Autogenerated by Thrift Compiler (0.8.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package gen;
import java.util.Map;
import java.util.HashMap;
import org.apache.thrift.TEnum;

public enum EventType implements org.apache.thrift.TEnum {
  TASK_STATE_EVENT(0),
  TASK_ACTION_EVENT(1),
  IMAGE_EVENT(2),
  HEARTBEAT_EVENT(3),
  EXIT_EXECUTOR_EVENT(4);

  private final int value;

  private EventType(int value) {
    this.value = value;
  }

  /**
   * Get the integer value of this enum value, as defined in the Thrift IDL.
   */
  public int getValue() {
    return value;
  }

  /**
   * Find a the enum type by its integer value, as defined in the Thrift IDL.
   * @return null if the value is not found.
   */
  public static EventType findByValue(int value) { 
    switch (value) {
      case 0:
        return TASK_STATE_EVENT;
      case 1:
        return TASK_ACTION_EVENT;
      case 2:
        return IMAGE_EVENT;
      case 3:
        return HEARTBEAT_EVENT;
      case 4:
        return EXIT_EXECUTOR_EVENT;
      default:
        return null;
    }
  }
}