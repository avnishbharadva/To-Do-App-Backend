package com.avnish.to_do_app.mapper;

import com.avnish.to_do_app.entity.Task;
import com.avnish.to_do_app.entity.TaskDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskDTO taskToTaskDTO(Task task);
    Task taskDTOtoTask(TaskDTO taskDTO);
}
