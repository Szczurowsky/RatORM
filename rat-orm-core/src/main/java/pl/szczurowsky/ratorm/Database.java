package pl.szczurowsky.ratorm;

import pl.szczurowsky.ratorm.credentials.Credentials;
import pl.szczurowsky.ratorm.exceptions.AlreadyConnectedException;
import pl.szczurowsky.ratorm.exceptions.ModelException;
import pl.szczurowsky.ratorm.exceptions.NotConnectedToDatabaseException;
import pl.szczurowsky.ratorm.exceptions.OperationException;
import pl.szczurowsky.ratorm.model.Model;
import pl.szczurowsky.ratorm.serializers.ComplexSerializer;
import pl.szczurowsky.ratorm.serializers.Serializer;
import pl.szczurowsky.ratorm.serializers.basic.*;
import pl.szczurowsky.ratorm.serializers.complex.CollectionSerializer;
import pl.szczurowsky.ratorm.serializers.complex.MapSerializer;

import java.time.Duration;
import java.util.*;

/**
 * Abstraction for database.
 * Provides core methods for all database types
 */
public abstract class Database {

    /**
     * HashMap with all serializers
     */
    protected final HashMap<Class<?>, Class<? extends Serializer<?>>> serializers = new HashMap<>();

    /**
     * HashMap with all complex serializers
     */
    protected final HashMap<Class<?>, Class<? extends ComplexSerializer>> complexSerializers = new HashMap<>();

    /**
     * HashMap of all initialized models
     */
    protected final HashMap<String, Class<? extends Model>> models = new HashMap<>();

    /**
     * Flag indicating whether database is connected
     */
    protected boolean connected = false;

    public Database() {
        registerSerializer(String.class, StringSerializer.class);
        registerSerializer(Integer.class, IntegerSerializer.class);
        registerSerializer(Long.class, LongSerializer.class);
        registerSerializer(Float.class, FloatSerializer.class);
        registerSerializer(Double.class, DoubleSerializer.class);
        registerSerializer(Boolean.class, BooleanSerializer.class);
        registerSerializer(Byte.class, ByteSerializer.class);
        registerSerializer(Character.class, CharacterSerializer.class);
        registerSerializer(Short.class, ShortSerializer.class);
        registerSerializer(UUID.class, UUIDSerializer.class);
        registerSerializer(Duration.class, DurationSerializer.class);

        registerComplexSerializer(AbstractMap.class, MapSerializer.class);
        registerComplexSerializer(AbstractList.class, CollectionSerializer.class);
    }

    /**
     * Registers serializer for given class
     * @param classType Class to register
     * @param serializer Class of serializer
     */
    public void registerSerializer(Class<?> classType, Class<? extends Serializer<?>> serializer) {
        serializers.put(classType, serializer);
    }

    /**
     * Registers complex serializer for given class
     * @param classType Class to register
     * @param serializer Class of serializer
     */
    public void registerComplexSerializer(Class<?> classType, Class<? extends ComplexSerializer> serializer) {
        complexSerializers.put(classType, serializer);
    }

    /**
     * Check if database is connected
     * @return true if database is connected
     */
    public abstract boolean isConnectionValid();

    /**
     * Terminate connection with database
     * @throws NotConnectedToDatabaseException If database is not connected
     */
    public abstract void terminateConnection() throws NotConnectedToDatabaseException;

    /**
     * Connects to database using URI
     * @param URI URI of database
     * @throws AlreadyConnectedException if database is already connected
     */
    public abstract void connect(String URI) throws AlreadyConnectedException;

    /**
     * Connects to database using credentials
     * @see Credentials
     * @param credentials Credentials for database
     * @throws AlreadyConnectedException if database is already connected
     */
    public abstract void connect(Credentials credentials) throws AlreadyConnectedException;

    /**
     * Initialize table in database. If table not existing in database than creating it. If exists than load
     * @param models Models to initialize
     * @throws ModelException if model is not valid
     */
    public abstract void initModel(Collection<Class<? extends Model>> models) throws ModelException;

    /**
     * Fetch all objects which matches model class
     * @param <T> Model generic type
     * @param model Model class
     * @throws OperationException if operation was not successful
     */
    public abstract <T extends Model> List<T> fetchAll(Class<T> model) throws OperationException;

    /**
     * Save object to database
     * @param model Model to save
     * @param <T> Model generic type
     * @throws OperationException if operation was not successful
     */
    public abstract <T extends Model> void save(T model) throws OperationException;

    /**
     * Save object to database
     * @param model Model to save
     * @param options Options for saving
     * @param <T> Model generic type
     * @throws OperationException if operation was not successful
     */
    public abstract <T extends Model> void save(T model, HashMap<String, Object> options) throws OperationException;

    /**
     * Save list of objects to database
     * @param model List of models to save
     * @param <T> Model generic type
     * @throws OperationException if operation was not successful
     */
    public abstract <T extends Model> void saveMany(Collection<T> model) throws OperationException;

    /**
     * Save list of objects to database
     * @param model List of models to save
     * @param options Options for saving
     * @param <T> Model generic type
     * @throws OperationException if operation was not successful
     */
    public abstract <T extends Model> void saveMany(Collection<T> model, HashMap<String, Object> options) throws OperationException;

    /**
     * Delete object from database
     * @param model Model to delete
     * @param <T> Model generic type
     * @throws OperationException if operation was not successful
     */
    public abstract <T extends Model> void delete(T model) throws OperationException;

    /**
     * Delete object from database
     * @param model Model to delete
     * @param options Options for deleting
     * @param <T> Model generic type
     * @throws OperationException if operation was not successful
     */
    public abstract <T extends Model> void delete(T model, HashMap<String, Object> options) throws OperationException;

    /**
     * Delete object from database
     * @param models Models to delete
     * @param <T> Model generic type
     * @throws OperationException if operation was not successful
     */
    public abstract <T extends Model> void deleteMany(Collection<T> models) throws OperationException;

    /**
     * Delete object from database
     * @param models Models to delete
     * @param options Options for deleting
     * @param <T> Model generic type
     * @throws OperationException if operation was not successful
     */
    public abstract <T extends Model> void deleteMany(Collection<T> models, HashMap<String, Object> options) throws OperationException;

}
