package logic.idgenerator;

import logic.idgenerator.exception.IdGeneratorCreatingException;
import logic.idgenerator.exception.IdGeneratorGenerationException;

import java.io.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class IdGenerator     //подумать что делать когда объектов много или сделать синглтоном
{
    private long nextGeneratedId;
    private final Lock lock;

    private IdGenerator(final long nextGeneratedId)
    {
        super();
        this.nextGeneratedId = nextGeneratedId;
        this.lock = new ReentrantLock();
    }

    public static IdGenerator createIdGenerator()
            throws IdGeneratorCreatingException
    {
        try(final FileInputStream fileInputStream = new FileInputStream(IdGenerator.PATH_OF_FILE_WITH_LAST_GENERATED_ID);
            final BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            final ObjectInputStream objectInputStream = new ObjectInputStream(bufferedInputStream))
        {
            final long lastGeneratedId = objectInputStream.readLong();
            final long nextGeneratedIdOfGenerator = lastGeneratedId + 1;
            return new IdGenerator(nextGeneratedIdOfGenerator);
        }
        catch(final EOFException eofException)  //when file is empty(id haven't been generated yet)
        {
            return new IdGenerator(IdGenerator.INITIAL_VALUE_OF_NEXT_GENERATED_ID);
        }
        catch(final IOException cause)
        {
            throw new IdGeneratorCreatingException(cause);
        }
    }

    private static final long INITIAL_VALUE_OF_NEXT_GENERATED_ID = 0;

    public final long generateId()
            throws IdGeneratorGenerationException
    {
        this.lock.lock();
        try
        {
            final long generatedId = this.nextGeneratedId;
            this.nextGeneratedId++;
            this.writeLastGeneratedId(generatedId);
            return generatedId;
        }
        finally
        {
            this.lock.unlock();
        }
    }

    private void writeLastGeneratedId(final long writtenId)
            throws IdGeneratorGenerationException
    {
        this.lock.lock();
        try(final FileOutputStream fileOutputStream = new FileOutputStream(IdGenerator.PATH_OF_FILE_WITH_LAST_GENERATED_ID);
            final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            final ObjectOutputStream objectInputStream = new ObjectOutputStream(bufferedOutputStream))
        {
            objectInputStream.writeLong(writtenId);
        }
        catch(final IOException cause)
        {
            throw new IdGeneratorGenerationException(cause);
        }
        finally
        {
            this.lock.unlock();
        }
    }
    //TODO: change path
    private static final String PATH_OF_FILE_WITH_LAST_GENERATED_ID = "C:\\Users\\Ania\\Desktop\\learning\\java\\универ\\2 курс 4 семестр\\labs\\решения\\seventh_lab_servlet\\src\\main\\resources\\idgenerator\\last_generated_id.txt";
}
