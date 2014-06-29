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

public enum AppState implements org.apache.thrift.TEnum {
  APP_WAITING(0),
  APP_STARTING(1),
  APP_RUNNING(2),
  APP_FINISHED(3),
  APP_MISSED(4),
  APP_FAILED(5);

  private final int value;

  private AppState(int value) {
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
  public static AppState findByValue(int value) { 
    switch (value) {
      case 0:
        return APP_WAITING;
      case 1:
        return APP_STARTING;
      case 2:
        return APP_RUNNING;
      case 3:
        return APP_FINISHED;
      case 4:
        return APP_MISSED;
      case 5:
        return APP_FAILED;
      default:
        return null;
    }
  }
}
