package io.github.buzzxu.spuddy.dal;

import io.github.buzzxu.spuddy.dal.jdbi.JdbiDao;
import io.github.buzzxu.spuddy.objects.Pager;
import io.github.buzzxu.spuddy.security.objects.BossUserInfo;
import io.github.buzzxu.spuddy.security.objects.User;
import io.github.buzzxu.spuddy.security.objects.UserInfo;
import io.github.buzzxu.spuddy.util.Dates;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.result.ResultIterable;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static io.github.buzzxu.spuddy.util.Dates.asLocalDateTime;

/**
 * @author xux
 * @date 2024年12月29日 17:24:19
 */
@RegisterBeanMapper(BossUserInfo.class)
public interface SysUserDao extends JdbiDao {

    String SQL_COLUMNS_TABLE = """
            ub.id,ub.org_id,ub.type,ub.user_name,ub.mobile,ub.email,nick_name,
            ub.real_name,ub.avatar,ub.status,ub.gender,ub.source,ub.verified,ub.operator,ub.created_at,ub.updated_at,r.id AS roleId,r.code AS roleCode,r.name AS roleName
            """;
    String SQL_COLUMNS = SQL_COLUMNS_TABLE+ """
            ,ub.password,ub.salt,ub.firstlogin,ub.merge,ub.is_2fa AS use2FA,ub.secret_2fa AS secret2FA
            """;
    String SQL_SELECT = "SELECT " + SQL_COLUMNS + " FROM t_user_base ub JOIN t_user_role ur ON ub.id = ur.user_id JOIN t_role r ON ur.role_id = r.id";

    String SQL_SELECT_TABLE = "SELECT " + SQL_COLUMNS_TABLE + " FROM t_user_base ub JOIN t_user_role ur ON ub.id = ur.user_id JOIN t_role r ON ur.role_id = r.id";




    default Pager<BossUserInfo> list(int pageNumber,int pageSize,  Map<String,Object> params){
        String sql = sql(params);
        return paginate(sql,pageNumber,pageSize,params,query -> query.map(rowMapper()));
    }



    default Optional<BossUserInfo> of(long userId){
        return withHandle(handle -> handle.select(SQL_SELECT+ " WHERE ub.id=? LIMIT 1",userId).map(rowMapper()).findFirst());
    }


    default Optional<BossUserInfo> of(long userId,int type){
        return withHandle(handle -> handle.select(SQL_SELECT+ " WHERE ub.id=? AND ub.type = ? LIMIT 1",userId,type).map(rowMapper()).findFirst());
    }

    default String sql(Map<String,Object> params){
        String sql = SQL_SELECT_TABLE + " WHERE ";

        if(params.containsKey("keywords")){
            sql += " (ub.user_name LIKE :keywords OR ub.mobile LIKE :keywords  OR ub.real_name LIKE :keywords) AND ";
            params.put("keywords", "%" + params.get("keywords") + "%");
        }
        if(params.containsKey("status")){
            sql += " ub.status = :status AND ";
        }
        if(params.containsKey("roleId")){
            sql += " r.id = :roleId AND ";
        }
        String startDate,endDate;
        if(params.containsKey("startDate") && params.containsKey("endDate")){
            startDate = params.get("startDate").toString();
            endDate = params.get("endDate").toString();
        }else{
            LocalDate today = LocalDate.now();
            startDate = today.plusMonths(-3).format(Dates.DATE_FORMAT_DATE);
            endDate = today.format(Dates.DATE_FORMAT_DATE);
        }
        params.put("startTime",Dates.unixtime(LocalDateTime.parse(startDate + " 00:00:00",Dates.DATE_FORMAT_DATETIME)));
        params.put("endTime",Dates.unixtime(LocalDateTime.parse(endDate + " 23:59:59",Dates.DATE_FORMAT_DATETIME)));
        sql += " ub.deleted = false AND (ub.updated_at >= :startTime AND ub.updated_at <= :endTime ) ORDER BY ub.updated_at DESC";
        return sql;
    }

    default RowMapper<BossUserInfo> rowMapper(){
        return (rs,ctx)->{
          BossUserInfo user = new BossUserInfo();
            user.setId(rs.getLong("id"));
            user.setOrgId(rs.getInt("org_id"));
            user.setUserName(rs.getString("user_name"));
            user.setMobile(rs.getString("mobile"));
            user.setEmail(rs.getString("email"));
            user.setGender(rs.getInt("gender"));
            user.setStatus(rs.getInt("status"));
            if(isExistColumn(rs,"password")){
                user.setPassword(rs.getString("password"));
            }
            if(isExistColumn(rs,"salt")){
                user.setSalt(rs.getString("salt"));
            }


            user.setRealName(rs.getString("real_name"));
            user.setNickName(rs.getString("nick_name"));
            user.setAvatar(rs.getString("avatar"));
            user.setSource(rs.getInt("source"));
            if(isExistColumn(rs,"firstlogin")){
                user.setFirstLogin(rs.getBoolean("firstlogin"));
            }
            user.setType(rs.getInt("type"));
            if(isExistColumn(rs,"merge")){
                user.setMerge(rs.getBoolean("merge"));
            }
            if(isExistColumn(rs,"use2FA")){
                user.setUse2FA(rs.getBoolean("use2FA"));
            }
            if(isExistColumn(rs,"secret2FA")){
                user.setSecret2FA(rs.getString("secret2FA"));
            }
            user.setCreatedAt(asLocalDateTime(Instant.ofEpochSecond(rs.getLong("created_at"))));
            user.setUpdatedAt(asLocalDateTime(Instant.ofEpochSecond(rs.getLong("updated_at"))));

            //角色
            user.setRoleId(rs.getInt("roleId"));
            user.setRoleCode(rs.getString("roleCode"));
            user.setRoleName(rs.getString("roleName"));
          return user;
        };
    }

    default boolean isExistColumn(ResultSet rs, String columnName) {
        try {
            rs.findColumn(columnName);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}
