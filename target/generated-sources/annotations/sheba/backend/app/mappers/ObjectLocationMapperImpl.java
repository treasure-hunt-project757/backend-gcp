package sheba.backend.app.mappers;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import sheba.backend.app.DTO.ObjectLocationDTO;
import sheba.backend.app.entities.ObjectLocation;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-02T17:57:21+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.7 (Oracle Corporation)"
)
@Component
public class ObjectLocationMapperImpl implements ObjectLocationMapper {

    @Override
    public ObjectLocationDTO objectToObjectDTO(ObjectLocation objectLocation) {
        if ( objectLocation == null ) {
            return null;
        }

        ObjectLocationDTO objectLocationDTO = new ObjectLocationDTO();

        objectLocationDTO.setObjectID( objectLocation.getObjectID() );
        objectLocationDTO.setName( objectLocation.getName() );

        return objectLocationDTO;
    }

    @Override
    public Object objectDTOToObject(ObjectLocationDTO objectDTO) {
        if ( objectDTO == null ) {
            return null;
        }

        Object object = new Object();

        return object;
    }
}
