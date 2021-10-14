package logic.entityinserterindatabase;

import entity.Entity;
import logic.entityinserterindatabase.exception.EntityInsertionInDataBaseException;

import java.sql.Connection;

public interface EntityInserterInDataBase<TypeOfInsertedEntity extends Entity>
{
    public abstract void insertEntity(final TypeOfInsertedEntity insertedEntity, final Connection connection)
            throws EntityInsertionInDataBaseException;
}
