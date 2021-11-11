package com.company.service.dao;

import com.company.common.Crud;
import com.company.service.common.IServiceDao;
import com.company.service.domain.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServiceDao extends Crud<Service> implements IServiceDao {


    public enum Fields{
        ID("id"),
        TITLE("title"),
        COST("cost"),
        DURATION("duration_days");

        private final String val; // name of field in db
        Fields(String val){this.val=val;}

        @Override
        public String toString() {return val;}

        public String toStringWithQM(){return val + "=?";}
    }

    public static final String table = "service";

    private final String getByTitleStatement = "SELECT * FROM " + table +
            " WHERE " + Fields.TITLE.toStringWithQM();

    private final String getByDurationStatement = "SELECT * FROM " + table +
            " WHERE " + Fields.DURATION.toStringWithQM();

    private final String getCostLessThanStatement = "SELECT * FROM " + table +
            " WHERE " + Fields.DURATION.toString() + "<?";

    public ServiceDao(Connection connection) {

        super(connection,"subscriber",
                Arrays.stream(ServiceDao.Fields.values())
                        .filter(w->!w.equals(ServiceDao.Fields.ID))
                        .map(ServiceDao.Fields::toString).toList(),
                ServiceDao.Fields.ID.toString());
    }

    @Override
    protected List<Service> getEntities(PreparedStatement statement)
            throws SQLException {


        List<Service> services = new ArrayList<>();
        ResultSet rs = statement.executeQuery();
        while (rs.next()){
            services.add(new Service(
                    rs.getLong(ServiceDao.Fields.ID.toString()),
                    rs.getString(Fields.TITLE.toString()),
                    rs.getInt(ServiceDao.Fields.COST.toString()),
                    rs.getInt(Fields.DURATION.toString()))
            );
        }
        return services;
    }

    @Override
    protected void fillStatement(PreparedStatement statement, Service entity)
            throws SQLException {

        statement.setString(1,entity.getTitle());
        statement.setInt(2,entity.getCost());
        statement.setInt(3,entity.getDuration());
    }



    @Override
    public List<Service> getByTitle(String title) throws SQLException {
        return getByStringField(getByTitleStatement,title);
    }

    @Override
    public List<Service> getCostMoreThan(int cost) throws SQLException {
        return getByIntField(getCostLessThanStatement,cost);
    }

    @Override
    public List<Service> getByDuration(int duration) throws SQLException {
        return getByIntField(getByDurationStatement,duration);
    }
}
