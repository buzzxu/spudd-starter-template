package io.github.buzzxu.spuddy.controllers;

import io.github.buzzxu.spuddy.controllers.system.SysVoMapStructs;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * @author xux
 * @date 2024年12月31日 21:46:45
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
        , unmappedTargetPolicy = ReportingPolicy.IGNORE
        , nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL
        , nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface VoMapStructs {
    VoMapStructs INSTANCE = Mappers.getMapper(VoMapStructs.class);
}
