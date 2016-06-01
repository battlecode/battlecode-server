// automatically generated, do not modify

package battlecode.schema;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
/**
 * If events are not otherwise delimited, this wrapper structure
 * allows a game to be stored in a single buffer.
 * The first event will be a GameHeader; the last event will be a GameFooter.
 * matchHeaders[0] is the index of the 0th match header in the event stream,
 * corresponding to matchFooters[0]. These indices allow quick traversal of
 * the file.
 */
public final class GameWrapper extends Table {
  public static GameWrapper getRootAsGameWrapper(ByteBuffer _bb) { return getRootAsGameWrapper(_bb, new GameWrapper()); }
  public static GameWrapper getRootAsGameWrapper(ByteBuffer _bb, GameWrapper obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__init(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public GameWrapper __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; return this; }

  /**
   * The series of events comprising the game.
   */
  public EventWrapper events(int j) { return events(new EventWrapper(), j); }
  public EventWrapper events(EventWrapper obj, int j) { int o = __offset(4); return o != 0 ? obj.__init(__indirect(__vector(o) + j * 4), bb) : null; }
  public int eventsLength() { int o = __offset(4); return o != 0 ? __vector_len(o) : 0; }
  /**
   * The indices of the headers of the matches, in order.
   */
  public long matchHeaders(int j) { int o = __offset(6); return o != 0 ? (long)bb.getInt(__vector(o) + j * 4) & 0xFFFFFFFFL : 0; }
  public int matchHeadersLength() { int o = __offset(6); return o != 0 ? __vector_len(o) : 0; }
  public ByteBuffer matchHeadersAsByteBuffer() { return __vector_as_bytebuffer(6, 4); }
  /**
   * The indices of the footers of the matches, in order.
   */
  public long matchFooters(int j) { int o = __offset(8); return o != 0 ? (long)bb.getInt(__vector(o) + j * 4) & 0xFFFFFFFFL : 0; }
  public int matchFootersLength() { int o = __offset(8); return o != 0 ? __vector_len(o) : 0; }
  public ByteBuffer matchFootersAsByteBuffer() { return __vector_as_bytebuffer(8, 4); }

  public static int createGameWrapper(FlatBufferBuilder builder,
      int eventsOffset,
      int matchHeadersOffset,
      int matchFootersOffset) {
    builder.startObject(3);
    GameWrapper.addMatchFooters(builder, matchFootersOffset);
    GameWrapper.addMatchHeaders(builder, matchHeadersOffset);
    GameWrapper.addEvents(builder, eventsOffset);
    return GameWrapper.endGameWrapper(builder);
  }

  public static void startGameWrapper(FlatBufferBuilder builder) { builder.startObject(3); }
  public static void addEvents(FlatBufferBuilder builder, int eventsOffset) { builder.addOffset(0, eventsOffset, 0); }
  public static int createEventsVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startEventsVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static void addMatchHeaders(FlatBufferBuilder builder, int matchHeadersOffset) { builder.addOffset(1, matchHeadersOffset, 0); }
  public static int createMatchHeadersVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addInt(data[i]); return builder.endVector(); }
  public static void startMatchHeadersVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static void addMatchFooters(FlatBufferBuilder builder, int matchFootersOffset) { builder.addOffset(2, matchFootersOffset, 0); }
  public static int createMatchFootersVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addInt(data[i]); return builder.endVector(); }
  public static void startMatchFootersVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static int endGameWrapper(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
};
