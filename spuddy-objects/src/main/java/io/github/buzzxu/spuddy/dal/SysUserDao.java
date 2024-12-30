package io.github.buzzxu.spuddy.dal;

import io.github.buzzxu.spuddy.dal.jdbi.JdbiDao;
import io.github.buzzxu.spuddy.objects.Pager;
import io.github.buzzxu.spuddy.security.objects.BossUserInfo;
import io.github.buzzxu.spuddy.security.objects.User;
import io.github.buzzxu.spuddy.security.objects.UserInfo;
import io.github.buzzxu.spuddy.util.Dates;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

/**
 * @author xux
 * @date 2024年12月29日 17:24:19
 */
@RegisterBeanMapper(BossUserInfo.class)
public interface SysUserDao extends JdbiDao {

    String SQL_COLUMNS_TABLE = """
            ub.id,ub.org_id,ub.type,ub.user_name,ub.mobile,ub.email,nick_name AS nickName,
            ub.real_name,ub.avatar,ub.status,ub.gender,ub.source,ub.verified,ub.operator,ub.created_at,ub.updated_at,r.id AS roleId,r.code AS roleCode,r.name AS roleName
            """;
    String SQL_COLUMNS = """
            ub.password,ub.salt,ub.firstlogin,ub.merge,ub.is_2fa AS use2FA,ub.secret_2fa AS secret2FA
            """;
    String SQL_SELECT = "SELECT " + SQL_COLUMNS + " FROM t_user_base ub JOIN t_user_role ur ON ub.id = ur.user_id JOIN t_role r ON ur.role_id = r.id";

    String SQL_SELECT_TABLE = "SELECT " + SQL_COLUMNS_TABLE + " FROM t_user_base ub JOIN t_user_role ur ON ub.id = ur.user_id JOIN t_role r ON ur.role_id = r.id";




    default Pager<BossUserInfo> list(int pageNumber,int pageSize,  Map<String,Object> params){
        String sql = sql(params);
        return paginate(sql,pageNumber,pageSize,params,BossUserInfo.class);
    }


    @SqlQuery(SQL_SELECT + " WHERE ub.id=? LIMIT 1")
    Optional<BossUserInfo> of(long userId);

    @SqlQuery(SQL_SELECT+ " WHERE ub.id=? AND ub.type = ? LIMIT 1")
    Optional<BossUserInfo> of(long userId,int type);

    default String sql(Map<String,Object> params){
        String sql = SQL_SELECT_TABLE + " WHERE ";

        if(params.containsKey("keywords")){
            sql += " (ub.user_name LIKE :keywords OR ub.mobile LIKE :keywords) AND ";
            params.put("keywords", "%" + params.get("keywords") + "%");
        }
        if(params.containsKey("status")){
            sql += " ub.status = :status AND ";
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
        params.put("startTime",Dates.unixtime(LocalDateTime.parse(startDate,Dates.DATE_FORMAT_DATETIME)));
        params.put("endTime",Dates.unixtime(LocalDateTime.parse(endDate,Dates.DATE_FORMAT_DATETIME)));
        sql += " ub.deleted = 0 AND (updated_at >= :startTime AND updated_at <= endTime ) ORDER BY ub.updated_at DESC";
        return sql;
    }
}
