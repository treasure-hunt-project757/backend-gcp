package sheba.backend.app.mappers;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import sheba.backend.app.DTO.LocationDTO;
import sheba.backend.app.entities.Location;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-02T17:57:21+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.7 (Oracle Corporation)"
)
@Component
public class LocationMapperImpl implements LocationMapper {

    @Override
    public LocationDTO locationToLocationDTO(Location location) {
        if ( location == null ) {
            return null;
        }

        LocationDTO locationDTO = new LocationDTO();

        locationDTO.setLocationID( location.getLocationID() );
        locationDTO.setName( location.getName() );
        locationDTO.setFloor( location.getFloor() );
        locationDTO.setQRCodePublicUrl( location.getQRCodePublicUrl() );
        locationDTO.setLocationImagePublicUrl( location.getLocationImagePublicUrl() );

        return locationDTO;
    }
}
