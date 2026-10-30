/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.bcel;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.CharConversionException;
import java.io.Closeable;
import java.io.Console;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.Externalizable;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilePermission;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.FilterReader;
import java.io.FilterWriter;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.InvalidClassException;
import java.io.InvalidObjectException;
import java.io.LineNumberReader;
import java.io.NotActiveException;
import java.io.NotSerializableException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectInputValidation;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.ObjectStreamException;
import java.io.ObjectStreamField;
import java.io.OptionalDataException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.PushbackInputStream;
import java.io.PushbackReader;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.SequenceInputStream;
import java.io.Serializable;
import java.io.SerializablePermission;
import java.io.StreamCorruptedException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.SyncFailedException;
import java.io.UTFDataFormatException;
import java.io.UncheckedIOException;
import java.io.UnsupportedEncodingException;
import java.io.WriteAbortedException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.annotation.AnnotationFormatError;
import java.lang.annotation.AnnotationTypeMismatchException;
import java.lang.annotation.Documented;
import java.lang.annotation.IncompleteAnnotationException;
import java.lang.annotation.Inherited;
import java.lang.annotation.Native;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandleInfo;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MutableCallSite;
import java.lang.invoke.SwitchPoint;
import java.lang.invoke.VolatileCallSite;
import java.lang.invoke.WrongMethodTypeException;
import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.AnnotatedArrayType;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.AnnotatedTypeVariable;
import java.lang.reflect.AnnotatedWildcardType;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.ReflectPermission;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.UndeclaredThrowableException;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.Authenticator;
import java.net.BindException;
import java.net.CacheRequest;
import java.net.CacheResponse;
import java.net.ConnectException;
import java.net.ContentHandler;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.DatagramSocketImpl;
import java.net.FileNameMap;
import java.net.HttpCookie;
import java.net.HttpRetryException;
import java.net.HttpURLConnection;
import java.net.IDN;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.MulticastSocket;
import java.net.NetPermission;
import java.net.NetworkInterface;
import java.net.NoRouteToHostException;
import java.net.PasswordAuthentication;
import java.net.PortUnreachableException;
import java.net.ProtocolException;
import java.net.ProxySelector;
import java.net.ResponseCache;
import java.net.SecureCacheResponse;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketImpl;
import java.net.SocketOption;
import java.net.SocketOptions;
import java.net.SocketPermission;
import java.net.SocketTimeoutException;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.URLStreamHandler;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;
import java.nio.Buffer;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.InvalidMarkException;
import java.nio.LongBuffer;
import java.nio.MappedByteBuffer;
import java.nio.ReadOnlyBufferException;
import java.nio.ShortBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnmappableCharacterException;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.AccessDeniedException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.ClosedDirectoryStreamException;
import java.nio.file.ClosedFileSystemException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemException;
import java.nio.file.FileSystemLoopException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;
import java.nio.file.NotLinkException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.ProviderMismatchException;
import java.nio.file.ProviderNotFoundException;
import java.nio.file.ReadOnlyFileSystemException;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.Watchable;
import java.nio.file.attribute.AclEntry;
import java.nio.file.attribute.AclEntryFlag;
import java.nio.file.attribute.AclEntryPermission;
import java.nio.file.attribute.AclEntryType;
import java.nio.file.attribute.AclFileAttributeView;
import java.nio.file.attribute.AttributeView;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.DosFileAttributeView;
import java.nio.file.attribute.DosFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.FileStoreAttributeView;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.GroupPrincipal;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.nio.file.attribute.UserPrincipal;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.security.AccessControlContext;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.AlgorithmParameterGenerator;
import java.security.AlgorithmParameters;
import java.security.AlgorithmParametersSpi;
import java.security.AllPermission;
import java.security.AuthProvider;
import java.security.BasicPermission;
import java.security.Certificate;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.security.DigestException;
import java.security.DigestInputStream;
import java.security.DigestOutputStream;
import java.security.DomainCombiner;
import java.security.GeneralSecurityException;
import java.security.Guard;
import java.security.GuardedObject;
import java.security.Identity;
import java.security.IdentityScope;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.Key;
import java.security.KeyException;
import java.security.KeyFactory;
import java.security.KeyFactorySpi;
import java.security.KeyManagementException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyPairGeneratorSpi;
import java.security.KeyRep;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.KeyStoreSpi;
import java.security.MessageDigest;
import java.security.MessageDigestSpi;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;
import java.security.PolicySpi;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.ProtectionDomain;
import java.security.Provider;
import java.security.ProviderException;
import java.security.PublicKey;
import java.security.SecureClassLoader;
import java.security.SecureRandom;
import java.security.SecureRandomSpi;
import java.security.Security;
import java.security.SecurityPermission;
import java.security.Signature;
import java.security.SignatureException;
import java.security.SignatureSpi;
import java.security.SignedObject;
import java.security.Signer;
import java.security.UnrecoverableEntryException;
import java.security.UnrecoverableKeyException;
import java.security.UnresolvedPermission;
import java.sql.BatchUpdateException;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DataTruncation;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLClientInfoException;
import java.sql.SQLData;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLInput;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLInvalidAuthorizationSpecException;
import java.sql.SQLNonTransientConnectionException;
import java.sql.SQLNonTransientException;
import java.sql.SQLOutput;
import java.sql.SQLPermission;
import java.sql.SQLRecoverableException;
import java.sql.SQLSyntaxErrorException;
import java.sql.SQLTimeoutException;
import java.sql.SQLTransactionRollbackException;
import java.sql.SQLTransientConnectionException;
import java.sql.SQLTransientException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.sql.Wrapper;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.text.Bidi;
import java.text.BreakIterator;
import java.text.CharacterIterator;
import java.text.ChoiceFormat;
import java.text.CollationElementIterator;
import java.text.CollationKey;
import java.text.Collator;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.FieldPosition;
import java.text.Format;
import java.text.MessageFormat;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.RuleBasedCollator;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.time.Clock;
import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.MonthDay;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Period;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.chrono.AbstractChronology;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.ChronoPeriod;
import java.time.chrono.ChronoZonedDateTime;
import java.time.chrono.Chronology;
import java.time.chrono.Era;
import java.time.chrono.HijrahChronology;
import java.time.chrono.HijrahDate;
import java.time.chrono.HijrahEra;
import java.time.chrono.IsoChronology;
import java.time.chrono.IsoEra;
import java.time.chrono.JapaneseChronology;
import java.time.chrono.JapaneseDate;
import java.time.chrono.JapaneseEra;
import java.time.chrono.MinguoChronology;
import java.time.chrono.MinguoDate;
import java.time.chrono.MinguoEra;
import java.time.chrono.ThaiBuddhistChronology;
import java.time.chrono.ThaiBuddhistDate;
import java.time.chrono.ThaiBuddhistEra;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.DecimalStyle;
import java.time.format.FormatStyle;
import java.time.format.ResolverStyle;
import java.time.format.SignStyle;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.IsoFields;
import java.time.temporal.JulianFields;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalQueries;
import java.time.temporal.TemporalQuery;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.time.temporal.WeekFields;
import java.time.zone.ZoneOffsetTransition;
import java.time.zone.ZoneOffsetTransitionRule;
import java.time.zone.ZoneRules;
import java.time.zone.ZoneRulesException;
import java.time.zone.ZoneRulesProvider;
import java.util.AbstractCollection;
import java.util.AbstractList;
import java.util.AbstractMap;
import java.util.AbstractQueue;
import java.util.AbstractSequentialList;
import java.util.AbstractSet;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Currency;
import java.util.Date;
import java.util.Deque;
import java.util.Dictionary;
import java.util.DoubleSummaryStatistics;
import java.util.DuplicateFormatFlagsException;
import java.util.EmptyStackException;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.EventListenerProxy;
import java.util.EventObject;
import java.util.FormatFlagsConversionMismatchException;
import java.util.Formattable;
import java.util.FormattableFlags;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.IllegalFormatCodePointException;
import java.util.IllegalFormatConversionException;
import java.util.IllegalFormatException;
import java.util.IllegalFormatFlagsException;
import java.util.IllegalFormatPrecisionException;
import java.util.IllegalFormatWidthException;
import java.util.IllformedLocaleException;
import java.util.InputMismatchException;
import java.util.IntSummaryStatistics;
import java.util.InvalidPropertiesFormatException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.MissingFormatArgumentException;
import java.util.MissingFormatWidthException;
import java.util.MissingResourceException;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.PrimitiveIterator;
import java.util.PriorityQueue;
import java.util.Properties;
import java.util.PropertyPermission;
import java.util.PropertyResourceBundle;
import java.util.Queue;
import java.util.Random;
import java.util.RandomAccess;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.SimpleTimeZone;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.SplittableRandom;
import java.util.Stack;
import java.util.StringJoiner;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TooManyListenersException;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;
import java.util.UnknownFormatConversionException;
import java.util.UnknownFormatFlagsException;
import java.util.Vector;
import java.util.WeakHashMap;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CountedCompleter;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.Phaser;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.Semaphore;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TransferQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.atomic.AtomicStampedReference;
import java.util.concurrent.atomic.DoubleAccumulator;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.AbstractOwnableSynchronizer;
import java.util.concurrent.locks.AbstractQueuedLongSynchronizer;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;
import java.util.function.DoubleSupplier;
import java.util.function.DoubleToIntFunction;
import java.util.function.DoubleToLongFunction;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.LongBinaryOperator;
import java.util.function.LongConsumer;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;
import java.util.function.LongSupplier;
import java.util.function.LongToDoubleFunction;
import java.util.function.LongToIntFunction;
import java.util.function.LongUnaryOperator;
import java.util.function.ObjDoubleConsumer;
import java.util.function.ObjIntConsumer;
import java.util.function.ObjLongConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleBiFunction;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntBiFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongBiFunction;
import java.util.function.ToLongFunction;
import java.util.function.UnaryOperator;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.BaseStream;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Java8PublicClasses {

    // java.io
    private static final Class<?>[] JAVA_IO = { BufferedInputStream.class, BufferedOutputStream.class, BufferedReader.class, BufferedWriter.class,
            ByteArrayInputStream.class, ByteArrayOutputStream.class, CharArrayReader.class, CharArrayWriter.class, Closeable.class, Console.class,
            DataInput.class, DataInputStream.class, DataOutput.class, DataOutputStream.class, File.class, FileDescriptor.class, FileInputStream.class,
            FileOutputStream.class, FilePermission.class, FileReader.class, FileWriter.class, FilterInputStream.class, FilterOutputStream.class,
            FilterReader.class, FilterWriter.class, Flushable.class, InputStream.class, InputStreamReader.class, LineNumberReader.class, ObjectInput.class,
            ObjectInputStream.class, ObjectOutput.class, ObjectOutputStream.class, ObjectStreamClass.class, ObjectStreamField.class, OutputStream.class,
            OutputStreamWriter.class, PipedInputStream.class, PipedOutputStream.class, PipedReader.class, PipedWriter.class, PrintStream.class,
            PrintWriter.class, PushbackInputStream.class, PushbackReader.class, RandomAccessFile.class, Reader.class, SequenceInputStream.class,
            Serializable.class, SerializablePermission.class, StreamTokenizer.class, StringReader.class, StringWriter.class, Writer.class, Externalizable.class,
            FileFilter.class, FilenameFilter.class, ObjectInputValidation.class,
            // Exceptions
            CharConversionException.class, EOFException.class, FileNotFoundException.class, InterruptedIOException.class, InvalidClassException.class,
            InvalidObjectException.class, IOException.class, NotActiveException.class, NotSerializableException.class, ObjectStreamException.class,
            OptionalDataException.class, StreamCorruptedException.class, SyncFailedException.class, UnsupportedEncodingException.class,
            UTFDataFormatException.class, WriteAbortedException.class, UncheckedIOException.class };

    // java.lang
    private static final Class<?>[] JAVA_LANG = { Appendable.class, AutoCloseable.class, Boolean.class, Byte.class, CharSequence.class, Character.class,
            Class.class, ClassLoader.class, ClassValue.class, Cloneable.class, Comparable.class, /* Compiler.class, */ Double.class, Enum.class, Float.class,
            InheritableThreadLocal.class, Integer.class, Iterable.class, Long.class, Math.class, Number.class, Object.class, Package.class, Process.class,
            ProcessBuilder.class, Readable.class, Runnable.class, Runtime.class, RuntimePermission.class, SecurityManager.class, Short.class,
            StackTraceElement.class, StrictMath.class, String.class, StringBuffer.class, StringBuilder.class, System.class, Thread.class, ThreadGroup.class,
            ThreadLocal.class, Throwable.class, Void.class,
            // Exceptions
            ArithmeticException.class, ArrayIndexOutOfBoundsException.class, ArrayStoreException.class, ClassCastException.class, ClassNotFoundException.class,
            CloneNotSupportedException.class, EnumConstantNotPresentException.class, Exception.class, IllegalAccessException.class,
            IllegalArgumentException.class, IllegalMonitorStateException.class, IllegalStateException.class, IllegalThreadStateException.class,
            IndexOutOfBoundsException.class, InstantiationException.class, InterruptedException.class, NegativeArraySizeException.class,
            NoSuchFieldException.class, NoSuchMethodException.class, NullPointerException.class, NumberFormatException.class,
            ReflectiveOperationException.class, RuntimeException.class, SecurityException.class, StringIndexOutOfBoundsException.class,
            TypeNotPresentException.class, UnsupportedOperationException.class,
            // Errors
            AbstractMethodError.class, AssertionError.class, BootstrapMethodError.class, ClassCircularityError.class, ClassFormatError.class, Error.class,
            ExceptionInInitializerError.class, IllegalAccessError.class, IncompatibleClassChangeError.class, InstantiationError.class, InternalError.class,
            LinkageError.class, NoClassDefFoundError.class, NoSuchFieldError.class, NoSuchMethodError.class, OutOfMemoryError.class, StackOverflowError.class,
            ThreadDeath.class, UnknownError.class, UnsatisfiedLinkError.class, UnsupportedClassVersionError.class, VerifyError.class,
            VirtualMachineError.class };

    // java.lang.annotation
    private static final Class<?>[] JAVA_LANG_ANNOTATION = { Annotation.class, Documented.class, Inherited.class, Retention.class, Target.class,
            AnnotationFormatError.class, AnnotationTypeMismatchException.class, IncompleteAnnotationException.class, Repeatable.class, Native.class };

    // java.lang.invoke
    private static final Class<?>[] JAVA_LANG_INVOKE = { CallSite.class, ConstantCallSite.class, MethodHandle.class, MethodHandleInfo.class,
            MethodHandles.class, MethodType.class, MutableCallSite.class, SwitchPoint.class, VolatileCallSite.class, WrongMethodTypeException.class,
            LambdaMetafactory.class };

    // java.lang.ref
    private static final Class<?>[] JAVA_LANG_REF = { PhantomReference.class, Reference.class, ReferenceQueue.class, SoftReference.class,
            WeakReference.class };

    // java.lang.reflect
    private static final Class<?>[] JAVA_LANG_REFLECT = { AccessibleObject.class, AnnotatedElement.class, Array.class, Constructor.class,
            Executable.class, Field.class, GenericArrayType.class, GenericDeclaration.class, InvocationHandler.class, Member.class, Method.class,
            Modifier.class, Parameter.class, ParameterizedType.class, Proxy.class, ReflectPermission.class, Type.class, TypeVariable.class, WildcardType.class,
            AnnotatedType.class, AnnotatedArrayType.class, AnnotatedParameterizedType.class, AnnotatedTypeVariable.class, AnnotatedWildcardType.class,
            InvocationTargetException.class, MalformedParameterizedTypeException.class, UndeclaredThrowableException.class };

    // java.math
    private static final Class<?>[] JAVA_MATH = { BigDecimal.class, BigInteger.class, MathContext.class, RoundingMode.class };

    // java.net
    private static final Class<?>[] JAVA_NET = { Authenticator.class, CacheRequest.class, CacheResponse.class, ContentHandler.class,
            CookieHandler.class, CookieManager.class, CookiePolicy.class, CookieStore.class, DatagramPacket.class, DatagramSocket.class,
            DatagramSocketImpl.class, FileNameMap.class, HttpCookie.class, HttpURLConnection.class, IDN.class, Inet4Address.class, Inet6Address.class,
            InetAddress.class, InetSocketAddress.class, InterfaceAddress.class, JarURLConnection.class, MulticastSocket.class, NetPermission.class,
            NetworkInterface.class, PasswordAuthentication.class, Proxy.class, ProxySelector.class, ResponseCache.class, SecureCacheResponse.class,
            ServerSocket.class, Socket.class, SocketAddress.class, SocketImpl.class, SocketOption.class, SocketOptions.class, SocketPermission.class,
            StandardProtocolFamily.class, StandardSocketOptions.class, URI.class, URL.class, URLClassLoader.class, URLConnection.class, URLDecoder.class,
            URLEncoder.class, URLStreamHandler.class, BindException.class, ConnectException.class, HttpRetryException.class, MalformedURLException.class,
            NoRouteToHostException.class, PortUnreachableException.class, ProtocolException.class, SocketException.class, SocketTimeoutException.class,
            UnknownHostException.class, UnknownServiceException.class, URISyntaxException.class };

    // java.nio
    private static final Class<?>[] JAVA_NIO = { Buffer.class, ByteBuffer.class, ByteOrder.class, CharBuffer.class, DoubleBuffer.class,
            FloatBuffer.class, IntBuffer.class, LongBuffer.class, MappedByteBuffer.class, ShortBuffer.class, BufferOverflowException.class,
            BufferUnderflowException.class, InvalidMarkException.class, ReadOnlyBufferException.class };

    // java.nio.charset
    private static final Class<?>[] JAVA_NIO_CHARSET = { Charset.class, CharsetDecoder.class, CharsetEncoder.class, CoderResult.class,
            CodingErrorAction.class, StandardCharsets.class, CharacterCodingException.class, IllegalCharsetNameException.class, MalformedInputException.class,
            UnmappableCharacterException.class, UnsupportedCharsetException.class };

    // java.nio.file
    private static final Class<?>[] JAVA_NIO_FILE = { DirectoryStream.class, FileSystem.class, FileSystems.class, FileVisitOption.class,
            FileVisitResult.class, FileVisitor.class, Files.class, LinkOption.class, OpenOption.class, Path.class, PathMatcher.class, Paths.class,
            SimpleFileVisitor.class, StandardCopyOption.class, StandardOpenOption.class, StandardWatchEventKinds.class, WatchEvent.class, WatchKey.class,
            WatchService.class, Watchable.class, AccessDeniedException.class, AtomicMoveNotSupportedException.class, ClosedDirectoryStreamException.class,
            ClosedFileSystemException.class, ClosedWatchServiceException.class, DirectoryIteratorException.class, DirectoryNotEmptyException.class,
            FileAlreadyExistsException.class, FileSystemException.class, FileSystemLoopException.class, FileSystemNotFoundException.class,
            InvalidPathException.class, NoSuchFileException.class, NotDirectoryException.class, NotLinkException.class, ProviderMismatchException.class,
            ProviderNotFoundException.class, ReadOnlyFileSystemException.class };

    // java.nio.file.attribute
    private static final Class<?>[] JAVA_NIO_FILE_ATTRIBUTE = { AclEntry.class, AclEntryFlag.class, AclEntryPermission.class, AclEntryType.class,
            AclFileAttributeView.class, AttributeView.class, BasicFileAttributeView.class, BasicFileAttributes.class, DosFileAttributeView.class,
            DosFileAttributes.class, FileAttribute.class, FileAttributeView.class, FileOwnerAttributeView.class, FileStoreAttributeView.class, FileTime.class,
            GroupPrincipal.class, PosixFileAttributeView.class, PosixFileAttributes.class, PosixFilePermission.class, PosixFilePermissions.class,
            UserDefinedFileAttributeView.class, UserPrincipal.class, UserPrincipalLookupService.class, UserPrincipalNotFoundException.class };

    // java.security
    private static final Class<?>[] JAVA_SECURITY = { AccessControlContext.class, AccessController.class, AlgorithmParameterGenerator.class,
            AlgorithmParameters.class, AlgorithmParametersSpi.class, AllPermission.class, AuthProvider.class, BasicPermission.class, Certificate.class,
            CodeSigner.class, CodeSource.class, DigestInputStream.class, DigestOutputStream.class, DomainCombiner.class, Guard.class, GuardedObject.class,
            Identity.class, IdentityScope.class, Key.class, KeyFactory.class, KeyFactorySpi.class, KeyPair.class, KeyPairGenerator.class,
            KeyPairGeneratorSpi.class, KeyRep.class, KeyStore.class, KeyStoreSpi.class, MessageDigest.class, MessageDigestSpi.class, Permission.class,
            PermissionCollection.class, Permissions.class, Policy.class, PolicySpi.class, Principal.class, PrivateKey.class, PrivilegedAction.class,
            PrivilegedActionException.class, PrivilegedExceptionAction.class, ProtectionDomain.class, Provider.class, PublicKey.class, SecureClassLoader.class,
            SecureRandom.class, SecureRandomSpi.class, Security.class, SecurityPermission.class, Signature.class, SignatureException.class, SignatureSpi.class,
            SignedObject.class, Signer.class, java.security.Timestamp.class, UnrecoverableEntryException.class, UnrecoverableKeyException.class,
            UnresolvedPermission.class, AccessControlException.class, DigestException.class, GeneralSecurityException.class,
            InvalidAlgorithmParameterException.class, InvalidKeyException.class, InvalidParameterException.class, KeyException.class,
            KeyManagementException.class, KeyStoreException.class, NoSuchAlgorithmException.class, NoSuchProviderException.class, ProviderException.class };

    // java.sql
    private static final Class<?>[] JAVA_SQL = { Array.class, Blob.class, CallableStatement.class, Clob.class, Connection.class, DatabaseMetaData.class,
            java.sql.Date.class, Driver.class, DriverManager.class, DriverPropertyInfo.class, NClob.class, ParameterMetaData.class, PreparedStatement.class,
            Ref.class, ResultSet.class, ResultSetMetaData.class, RowId.class, Savepoint.class, SQLData.class, SQLInput.class, SQLOutput.class,
            SQLPermission.class, SQLXML.class, Statement.class, Struct.class, Time.class, Timestamp.class, Types.class, Wrapper.class,
            BatchUpdateException.class, DataTruncation.class, SQLClientInfoException.class, SQLException.class, SQLDataException.class,
            SQLFeatureNotSupportedException.class, SQLIntegrityConstraintViolationException.class, SQLInvalidAuthorizationSpecException.class,
            SQLNonTransientConnectionException.class, SQLNonTransientException.class, SQLRecoverableException.class, SQLSyntaxErrorException.class,
            SQLTimeoutException.class, SQLTransactionRollbackException.class, SQLTransientConnectionException.class, SQLTransientException.class,
            SQLWarning.class };

    // java.text
    private static final Class<?>[] JAVA_TEXT = { Annotation.class, AttributedCharacterIterator.class, AttributedString.class, Bidi.class,
            BreakIterator.class, CharacterIterator.class, ChoiceFormat.class, CollationElementIterator.class, CollationKey.class, Collator.class,
            DateFormat.class, DateFormatSymbols.class, DecimalFormat.class, DecimalFormatSymbols.class, FieldPosition.class, Format.class, MessageFormat.class,
            Normalizer.class, NumberFormat.class, ParsePosition.class, RuleBasedCollator.class, SimpleDateFormat.class, StringCharacterIterator.class,
            ParseException.class };

    // java.time
    private static final Class<?>[] JAVA_TIME = { Clock.class, DateTimeException.class, DayOfWeek.class, Duration.class, Instant.class, LocalDate.class,
            LocalDateTime.class, LocalTime.class, Month.class, MonthDay.class, OffsetDateTime.class, OffsetTime.class, Period.class, Year.class,
            YearMonth.class, ZonedDateTime.class, ZoneId.class, ZoneOffset.class };

    // java.time.chrono
    private static final Class<?>[] JAVA_TIME_CHRONO = { AbstractChronology.class, ChronoLocalDate.class, ChronoLocalDateTime.class, Chronology.class,
            ChronoPeriod.class, ChronoZonedDateTime.class, Era.class, HijrahChronology.class, HijrahDate.class, HijrahEra.class, IsoChronology.class,
            IsoEra.class, JapaneseChronology.class, JapaneseDate.class, JapaneseEra.class, MinguoChronology.class, MinguoDate.class, MinguoEra.class,
            ThaiBuddhistChronology.class, ThaiBuddhistDate.class, ThaiBuddhistEra.class };

    // java.time.format
    private static final Class<?>[] JAVA_TIME_FORMAT = { DateTimeFormatter.class, DateTimeFormatterBuilder.class, DateTimeParseException.class,
            DecimalStyle.class, FormatStyle.class, ResolverStyle.class, SignStyle.class, TextStyle.class };

    // java.time.temporal
    private static final Class<?>[] JAVA_TIME_TEMPORAL = { ChronoField.class, ChronoUnit.class, IsoFields.class, JulianFields.class, Temporal.class,
            TemporalAccessor.class, TemporalAdjuster.class, TemporalAdjusters.class, TemporalAmount.class, TemporalField.class, TemporalQueries.class,
            TemporalQuery.class, TemporalUnit.class, ValueRange.class, WeekFields.class, UnsupportedTemporalTypeException.class };

    // java.time.zone
    private static final Class<?>[] JAVA_TIME_ZONE = { ZoneOffsetTransition.class, ZoneOffsetTransitionRule.class, ZoneRules.class,
            ZoneRulesException.class, ZoneRulesProvider.class };

    // java.util
    private static final Class<?>[] JAVA_UTIL = { AbstractCollection.class, AbstractList.class, AbstractMap.class, AbstractQueue.class,
            AbstractSequentialList.class, AbstractSet.class, ArrayDeque.class, ArrayList.class, Arrays.class, Base64.class, BitSet.class, Calendar.class,
            Collection.class, Collections.class, Comparator.class, Currency.class, Date.class, Deque.class, Dictionary.class, DoubleSummaryStatistics.class,
            EnumMap.class, EnumSet.class, Enumeration.class, EventListener.class, EventListenerProxy.class, EventObject.class, FormattableFlags.class,
            Formatter.class, GregorianCalendar.class, HashMap.class, HashSet.class, Hashtable.class, IdentityHashMap.class, IntSummaryStatistics.class,
            Iterator.class, LinkedHashMap.class, LinkedHashSet.class, LinkedList.class, List.class, ListIterator.class, ListResourceBundle.class, Locale.class,
            LongSummaryStatistics.class, Map.class, NavigableMap.class, NavigableSet.class, Objects.class, Observable.class, Observer.class, Optional.class,
            OptionalDouble.class, OptionalInt.class, OptionalLong.class, PrimitiveIterator.class, PriorityQueue.class, Properties.class,
            PropertyPermission.class, PropertyResourceBundle.class, Queue.class, Random.class, RandomAccess.class, ResourceBundle.class, Scanner.class,
            ServiceLoader.class, Set.class, SimpleTimeZone.class, SortedMap.class, SortedSet.class, Spliterator.class, Spliterators.class,
            SplittableRandom.class, Stack.class, StringJoiner.class, StringTokenizer.class, Timer.class, TimerTask.class, TimeZone.class, TreeMap.class,
            TreeSet.class, UUID.class, Vector.class, WeakHashMap.class, Formattable.class,
            // Exceptions
            ConcurrentModificationException.class, DuplicateFormatFlagsException.class, EmptyStackException.class, FormatFlagsConversionMismatchException.class,
            FormatterClosedException.class, IllegalFormatCodePointException.class, IllegalFormatConversionException.class, IllegalFormatException.class,
            IllegalFormatFlagsException.class, IllegalFormatPrecisionException.class, IllegalFormatWidthException.class, IllformedLocaleException.class,
            InputMismatchException.class, InvalidPropertiesFormatException.class, MissingFormatArgumentException.class, MissingFormatWidthException.class,
            MissingResourceException.class, NoSuchElementException.class, TooManyListenersException.class, UnknownFormatConversionException.class,
            UnknownFormatFlagsException.class, ServiceConfigurationError.class };

    // java.util.concurrent
    private static final Class<?>[] JAVA_UTIL_CONCURRENT = { AbstractExecutorService.class, ArrayBlockingQueue.class, BlockingDeque.class,
            BlockingQueue.class, Callable.class, CompletableFuture.class, CompletionException.class, CompletionService.class, ConcurrentHashMap.class,
            ConcurrentLinkedDeque.class, ConcurrentLinkedQueue.class, ConcurrentMap.class, ConcurrentNavigableMap.class, ConcurrentSkipListMap.class,
            ConcurrentSkipListSet.class, CopyOnWriteArrayList.class, CopyOnWriteArraySet.class, CountDownLatch.class, CountedCompleter.class,
            CyclicBarrier.class, Delayed.class, DelayQueue.class, Exchanger.class, Executor.class, ExecutorCompletionService.class, ExecutorService.class,
            Executors.class, ForkJoinPool.class, ForkJoinTask.class, ForkJoinWorkerThread.class, Future.class, FutureTask.class, LinkedBlockingDeque.class,
            LinkedBlockingQueue.class, LinkedTransferQueue.class, Phaser.class, PriorityBlockingQueue.class, RecursiveAction.class, RecursiveTask.class,
            RejectedExecutionHandler.class, RunnableFuture.class, RunnableScheduledFuture.class, ScheduledExecutorService.class, ScheduledFuture.class,
            ScheduledThreadPoolExecutor.class, Semaphore.class, SynchronousQueue.class, ThreadFactory.class, ThreadLocalRandom.class, ThreadPoolExecutor.class,
            TimeUnit.class, TransferQueue.class, BrokenBarrierException.class, CancellationException.class, CompletionException.class, ExecutionException.class,
            RejectedExecutionException.class, TimeoutException.class };

    // java.util.concurrent.atomic
    private static final Class<?>[] JAVA_UTIL_CONCURRENT_ATOMIC = { AtomicBoolean.class, AtomicInteger.class, AtomicIntegerArray.class,
            AtomicIntegerFieldUpdater.class, AtomicLong.class, AtomicLongArray.class, AtomicLongFieldUpdater.class, AtomicMarkableReference.class,
            AtomicReference.class, AtomicReferenceArray.class, AtomicReferenceFieldUpdater.class, AtomicStampedReference.class, DoubleAccumulator.class,
            DoubleAdder.class, LongAccumulator.class, LongAdder.class };

    // java.util.concurrent.locks
    private static final Class<?>[] JAVA_UTIL_CONCURRENT_LOCKS = { AbstractOwnableSynchronizer.class, AbstractQueuedLongSynchronizer.class,
            AbstractQueuedSynchronizer.class, Condition.class, Lock.class, LockSupport.class, ReadWriteLock.class, ReentrantLock.class,
            ReentrantReadWriteLock.class, StampedLock.class };

    // java.util.function
    private static final Class<?>[] JAVA_UTIL_FUNCTION = { BiConsumer.class, BiFunction.class, BinaryOperator.class, BiPredicate.class,
            BooleanSupplier.class, Consumer.class, DoubleBinaryOperator.class, DoubleConsumer.class, DoubleFunction.class, DoublePredicate.class,
            DoubleSupplier.class, DoubleToIntFunction.class, DoubleToLongFunction.class, DoubleUnaryOperator.class, Function.class, IntBinaryOperator.class,
            IntConsumer.class, IntFunction.class, IntPredicate.class, IntSupplier.class, IntToDoubleFunction.class, IntToLongFunction.class,
            IntUnaryOperator.class, LongBinaryOperator.class, LongConsumer.class, LongFunction.class, LongPredicate.class, LongSupplier.class,
            LongToDoubleFunction.class, LongToIntFunction.class, LongUnaryOperator.class, ObjDoubleConsumer.class, ObjIntConsumer.class, ObjLongConsumer.class,
            Predicate.class, Supplier.class, ToDoubleBiFunction.class, ToDoubleFunction.class, ToIntBiFunction.class, ToIntFunction.class,
            ToLongBiFunction.class, ToLongFunction.class, UnaryOperator.class };

    // java.util.regex
    private static final Class<?>[] JAVA_UTIL_REGEX = { MatchResult.class, Matcher.class, Pattern.class, PatternSyntaxException.class };

    // java.util.stream
    private static final Class<?>[] JAVA_UTIL_STREAM = { BaseStream.class, Collector.class, Collectors.class, DoubleStream.class, IntStream.class,
            LongStream.class, Stream.class, StreamSupport.class };

    private static final Class<?>[] ALL = addAll(JAVA_LANG, JAVA_LANG_ANNOTATION, JAVA_LANG_INVOKE,
            JAVA_LANG_REF, JAVA_LANG_REFLECT, JAVA_MATH, JAVA_IO, JAVA_NIO, JAVA_NIO_CHARSET,
            JAVA_NIO_FILE, JAVA_NIO_FILE_ATTRIBUTE, JAVA_UTIL, JAVA_UTIL_CONCURRENT, JAVA_UTIL_CONCURRENT_ATOMIC,
            JAVA_UTIL_CONCURRENT_LOCKS, JAVA_UTIL_FUNCTION, JAVA_UTIL_STREAM, JAVA_UTIL_REGEX, JAVA_TIME,
            JAVA_TIME_CHRONO, JAVA_TIME_FORMAT, JAVA_TIME_TEMPORAL, JAVA_TIME_ZONE, JAVA_NET, JAVA_TEXT,
            JAVA_SQL, JAVA_SECURITY);

    @SafeVarargs
    private static Class<?>[] addAll(final Class<?>[]... arrays) {
        int newLen = 0;
        for (final Class<?>[] array : arrays) {
            newLen += array.length;
        }
        final Class<?>[] result = new Class<?>[newLen];
        int i = 0;
        for (final Class<?>[] array : arrays) {
            System.arraycopy(array, 0, result, i, array.length);
            i += array.length;
        }
        return result;
    }

    public static Class<?>[] getAll() {
        return ALL.clone();
    }
}
