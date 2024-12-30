package io.github.buzzxu.spuddy.controllers.system;

import io.github.buzzxu.spuddy.controllers.system.requests.CreateOrUpdateSysteUserRequest;
import io.github.buzzxu.spuddy.controllers.system.responses.SysUserInfoResponse;
import io.github.buzzxu.spuddy.security.objects.BossUserInfo;
import io.github.buzzxu.spuddy.security.objects.UserInfo;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * @author xux
 * @date 2024年12月29日 16:58:44
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
        , unmappedTargetPolicy = ReportingPolicy.IGNORE
        , nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL
        , nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface VoMapStructs {
    VoMapStructs INSTANCE = Mappers.getMapper(VoMapStructs.class);


    @Mapping(target = "id",source = "id")
    @Mapping(target = "roleId",source = "roleId")
    @Mapping(target = "realName",expression = "java(obj.getRealName() == null ? null : obj.getRealName().strip())")
    @Mapping(target = "mobile",expression = "java(obj.getMobile() == null ? null : obj.getMobile().strip())")
    @Mapping(target = "userName",expression = "java(obj.getUserName() == null ? null : obj.getUserName().strip())")
    @Mapping(target = "email",source = "email")
    BossUserInfo to(CreateOrUpdateSysteUserRequest obj);

    @Mapping(target = "id",source = "id")
    @Mapping(target = "roleId",source = "roleId")
    @Mapping(target = "realName",expression = "java(obj.getRealName() == null ? null : obj.getRealName().strip())")
    @Mapping(target = "mobile",expression = "java(obj.getMobile() == null ? null : obj.getMobile().strip())")
    @Mapping(dependsOn = "mobile", target = "userName",expression = "java(obj.getUserName() == null ? obj.getMobile() : obj.getUserName().strip())")
    @Mapping(target = "email",source = "email")
    SysUserInfoResponse to(BossUserInfo obj);
}

