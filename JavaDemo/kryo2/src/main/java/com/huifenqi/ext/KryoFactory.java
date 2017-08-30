package com.huifenqi.ext;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.CompatibleFieldSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers;
import com.esotericsoftware.kryo.serializers.FieldSerializer;
import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer;
import de.javakaffee.kryoserializers.*;
import org.objenesis.strategy.StdInstantiatorStrategy;

import java.lang.reflect.InvocationHandler;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * Created by t3tiger on 2017/7/5.
 */
public class KryoFactory {

    public static Kryo createOrGetKryo() {
        return kryoThreadLocal.get();
    }

    private static final ThreadLocal<Kryo> kryoThreadLocal = new ThreadLocal<Kryo>() {
        @Override
        protected Kryo initialValue() {
            Kryo kryo = new MyKryo();
            /**
             * Objenesis 提供 StdInstantiatorStrategy，它使用JVM 特定的 API 来创建类的实例，而不会调用任何构造方法。
             */
            //kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
            /**
             * 在许多情况下，您可能希望有这样的策略：Kryo 首先尝试使用无参构造方法，如果尝试失败，
             * 再尝试使用 StdInstantiatorStrategy 作为后备方案，因为后备方案不需要调用任何构造方法。
             */
            kryo.setInstantiatorStrategy(new Kryo.DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));

            //kryo.setDefaultSerializer(TaggedFieldSerializer.class);
            kryo.setDefaultSerializer(CompatibleFieldSerializer.class);
            /**
             * 4.0.0的api
             */
            kryo.getFieldSerializerConfig().setCachedFieldNameStrategy(FieldSerializer.CachedFieldNameStrategy.EXTENDED);

            /**
             * 设置引用
             */
            kryo.setReferences(false);
            /**
             * 当 Kryo#setRegistrationRequired 设置为true，可在遇到任何未注册的类时抛出异常。这能阻止应用程序使用类名字符串来序列化。
             */
            kryo.setRegistrationRequired(true);

            kryo.register(Arrays.asList("").getClass(), new ArraysAsListSerializer());
            kryo.register(GregorianCalendar.class, new GregorianCalendarSerializer());
            kryo.register(InvocationHandler.class, new JdkProxySerializer());
            kryo.register(BigDecimal.class, new DefaultSerializers.BigDecimalSerializer());
            kryo.register(BigInteger.class, new DefaultSerializers.BigIntegerSerializer());
            kryo.register(Pattern.class, new RegexSerializer());
            kryo.register(BitSet.class, new BitSetSerializer());
            kryo.register(URI.class, new URISerializer());
            kryo.register(UUID.class, new UUIDSerializer());
            UnmodifiableCollectionsSerializer.registerSerializers(kryo);
            SynchronizedCollectionsSerializer.registerSerializers(kryo);

            // now just added some very common classes
            // TODO optimization
            kryo.register(HashMap.class, 100);
            kryo.register(ArrayList.class, 101);
            kryo.register(LinkedList.class, 102);
            kryo.register(HashSet.class, 103);
            kryo.register(TreeSet.class, 104);
            kryo.register(Hashtable.class, 105);
            kryo.register(Date.class, 106);
            kryo.register(Calendar.class, 107);
            kryo.register(ConcurrentHashMap.class, 108);
            kryo.register(SimpleDateFormat.class, 109);
            kryo.register(GregorianCalendar.class, 110);
            kryo.register(Vector.class, 111);
            kryo.register(BitSet.class, 112);
            kryo.register(StringBuffer.class, 113);
            kryo.register(StringBuilder.class, 114);
            kryo.register(Object.class, 115);
            kryo.register(Object[].class, 116);
            kryo.register(String[].class, 117);
            kryo.register(byte[].class, 118);
            kryo.register(char[].class, 119);
            kryo.register(int[].class, 120);
            kryo.register(float[].class, 121);
            kryo.register(double[].class, 122);

            return kryo;
        }
    };

}
