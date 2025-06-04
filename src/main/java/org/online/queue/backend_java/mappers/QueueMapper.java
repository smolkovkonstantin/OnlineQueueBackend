package org.online.queue.backend_java.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.online.queue.backend_java.mappers.config.MapperConfiguration;
import org.online.queue.backend_java.models.Position;
import org.online.queue.backend_java.models.PositionModel;
import org.online.queue.backend_java.models.Queue;
import org.online.queue.backend_java.models.QueueResponse;
import org.online.queue.backend_java.models.QueueResponseModel;

@Mapper(config = MapperConfiguration.class)
public interface QueueMapper {

    @Named("queueToQueueResponse")
    @Mapping(target = "queueId", source = "id")
    QueueResponse queueToQueueResponse(Queue queue);

    @Mapping(target = "queueSetting", source = "queue", qualifiedByName = "queueToQueueResponse")
    @Mapping(target = "positions", source = "queue.positions", qualifiedByName = "positionToPosition")
    QueueResponseModel queueToQueueResponseModel(Queue queue);

    @Named("positionToPosition")
    @Mapping(target = "firstName", source = "account.firstName")
    @Mapping(target = "lastName", source = "account.lastName")
    @Mapping(target = "userId", source = "account.id")
    @Mapping(target = "position", source = "queueNumber")
    PositionModel positionToPosition(Position position);
}
