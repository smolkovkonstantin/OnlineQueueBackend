package org.online.queue.backend_java.enums;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ActionQueue {
    CREATE_QUEUE("Queue \"%s\" created"),
    USER_ENTER_TO_QUEUE("%s enter to queue \"%s\" on position %s");

    String value;

    public String set(Object... values) {
        return String.format(this.value, values);
    }
}
