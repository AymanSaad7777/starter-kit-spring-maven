package com.client.core.base.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.json.JSONArray;

import com.bullhornsdk.data.api.BullhornData;
import com.bullhornsdk.data.model.entity.core.type.QueryEntity;
import com.bullhornsdk.data.model.entity.core.type.SearchEntity;
import com.bullhornsdk.data.model.parameter.QueryParams;
import com.bullhornsdk.data.model.parameter.SearchParams;
import com.bullhornsdk.data.model.parameter.standard.ParamFactory;
import com.bullhornsdk.data.model.response.list.ListWrapper;
import com.client.core.AppContext;
import com.google.common.collect.Lists;

public class Utility {

    private static final String SEARCH_DATE_FORMAT = "yyyyMMddHHmmss";

    public static String formatDateForSearch(DateTime value) {
        return value.toString(SEARCH_DATE_FORMAT);
    }

    public static String escapeWhitespaceForSearch(String value) {
        return value.replaceAll("\\s", "\\\\ ").replaceAll("\\)", "\\\\)").replaceAll("\\(", "\\\\(");
    }

    public static String escapeQuotesForQuery(String value) {
        return value.replaceAll("'", "''");
    }

	public static Map<String, String> commaDelimitedStringToMap(String commaDelimited) {
		List<String> parsed = Arrays.asList(commaDelimited.split(","));
		Map<String, String> theMap = new LinkedHashMap<String, String>();

		for(String value : parsed) {
			theMap.put(value, value);
		}

		return theMap;
	}

	public static void combineJsonArrays(JSONArray jsonArray1, JSONArray jsonArray2) {
		IntStream.range(0, jsonArray2.length()).forEach(index -> {
			jsonArray1.put(jsonArray2.get(index));
		});
	}

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();

        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

	public static Boolean isPositive(Integer value) {
	    return value != null && value > 0;
    }

    public static Boolean isPositive(BigDecimal value) {
        return value != null && value.compareTo(BigDecimal.ZERO) > 0;
    }

	public static Boolean isPositive(Object value) {
        return isPositive(forceParseBigDecimal(value));
    }

	public static BigDecimal parseBigDecimal(Object value) {
		if(value == null || value.toString().isEmpty()) {
			return null;
		} else {
			try {
				Double check = Double.parseDouble(value.toString());

				return new BigDecimal(check);
			} catch(NumberFormatException e) {
				return null;
			}
		}
	}

	public static BigDecimal forceParseBigDecimal(Object value) {
		if(value == null || value.toString().isEmpty()) {
			return BigDecimal.ZERO;
		} else {
			try {
				Double check = Double.parseDouble(value.toString());

				return new BigDecimal(check);
			} catch(NumberFormatException e) {
				return BigDecimal.ZERO;
			}
		}
	}

    public static Integer parseInteger(Object value) {
        if(value == null || value.toString().isEmpty()) {
            return null;
        } else {
            try {
                return Integer.parseInt(value.toString());
            } catch(NumberFormatException e) {
                return null;
            }
        }
    }

	public static Integer forceParseInteger(Object value) {
		if(value == null || value.toString().isEmpty()) {
			return 0;
		} else {
			try {
				return Integer.parseInt(value.toString());
			} catch(NumberFormatException e) {
				return 0;
			}
		}
	}

	public static Double parseDouble(Object value) {
		if(value == null || value.toString().isEmpty()) {
			return null;
		}else {
			try {
				return Double.parseDouble(value.toString());
			} catch(NumberFormatException e) {
				return null;
			}
		}
	}

	public static Double forceParseDouble(Object value) {
		if(value == null || value.toString().isEmpty()) {
			return 0D;
		}else {
			try {
				return Double.parseDouble(value.toString());
			} catch(NumberFormatException e) {
				return 0D;
			}
		}
	}

	public static Long forceParseLong(Object value) {
		if(value == null || value.toString().isEmpty()) {
			return 0L;
		} else {
			try {
				return Long.parseLong(value.toString());
			} catch(NumberFormatException e) {
				return 0L;
			}
		}
	}

	public static BigInteger forceParseBigInteger(Object value) {
		if(value == null || value.toString().isEmpty()) {
			return new BigInteger("0");
		} else {
			try {
				return new BigInteger(value.toString());
			} catch(NumberFormatException e) {
				return new BigInteger("0");
			}
		}
	}

	public static  Date parseStringToDate(String dateStr, String dateFormat) {
		if(dateStr == null || dateStr.isEmpty() || dateFormat == null || dateFormat.isEmpty()) {
			return null;
		} else {
			DateFormat format = new SimpleDateFormat(dateFormat);
			try {
				return format.parse(dateStr);
			} catch(ParseException e) {
				return null;
			}
		}
	}

	public static  Date forceParseStringToDate(String dateStr, String dateFormat) {
		if(dateStr == null || dateStr.isEmpty() || dateFormat == null || dateFormat.isEmpty()) {
            return DateTime.now().toDate();
		} else {
			TimeZone timeZone = TimeZone.getTimeZone("America/Detroit");
			DateFormat format = new SimpleDateFormat(dateFormat);
			format.setTimeZone(timeZone);
			format.setLenient(false);
			try {
				return format.parse(dateStr);
			} catch(ParseException e) {
				return new Date(0);
			}
		}
	}

	public static DateTime parseStringToDateTime(String dateStr, String dateFormat) {


	    Date date = parseStringToDate(dateStr, dateFormat);

	    if(date == null){
	        return null;
	    }

		DateTime now = Util.nowUsingTimeZone(null);

		DateTime theCorrectDate = new DateTime(date,DateTimeZone.UTC);
		theCorrectDate = theCorrectDate.plusHours(now.getHourOfDay());
		theCorrectDate = theCorrectDate.plusMinutes(now.getMinuteOfHour());
		return theCorrectDate;
	}



	public static DateTime forceParseStringToDateTime(String dateStr, String dateFormat) {
		return new DateTime(forceParseStringToDate(dateStr, dateFormat));
	}

	public static  XMLGregorianCalendar forceParseStringToXMLGregorianCalendar(String dateStr, String dateFormat) {
		return dateToXMLGregorianCal(forceParseStringToDate(dateStr, dateFormat));
	}

	public static String formatDate(Date date, String format) {
		if(date == null) {
			return "";
		}

		TimeZone timeZone = TimeZone.getTimeZone("America/Detroit");

		DateFormat dateFormat = new SimpleDateFormat(format);
		dateFormat.setLenient(false);
		dateFormat.setTimeZone(timeZone);
		return dateFormat.format(date);
	}

	public static String formatDateTime(DateTime date, String format) {
		if(date == null) {
			return "";
		}

		return formatDate(date.toDate(), format);
	}

	public static XMLGregorianCalendar dateToXMLGregorianCal(Date date) {
		if(date == null) {
			return  null;
		}

		TimeZone timeZone = TimeZone.getTimeZone("America/Detroit");

		GregorianCalendar gregorianCal = new GregorianCalendar(timeZone);
		gregorianCal.setTime(date);

		try {
			return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCal);
		} catch (DatatypeConfigurationException e) {
			return null;
		}
	}

	public static XMLGregorianCalendar dateToXMLGregorianCalWithTimezone(Date date, TimeZone timezone) {
		if(date == null) {
			return  null;
		}

		DateTimeZone dateTimeZone = DateTimeZone.forTimeZone(timezone);

		DateTime datetime = new DateTime(date.getTime(), dateTimeZone);

		return dateTimeToXmlGregorianCalendar(datetime);
	}

	public static Date xmlGregorianCalToDate(XMLGregorianCalendar xmlGregCal) {
		if(xmlGregCal == null) {
			return null;
		}
		return xmlGregCal.toGregorianCalendar().getTime();
	}

	public static Boolean stringContains(String toCheckIn, String toCheckFor) {
		if(toCheckIn == null || toCheckFor == null || toCheckFor.isEmpty()) {
			return false;
		}

		if(toCheckIn.isEmpty()) {
			return true;
		}

		List<String> valuesForCheck = Arrays.asList(toCheckIn.split(","));
		for(String value : valuesForCheck) {
			if(value.trim().equalsIgnoreCase(toCheckFor.trim())) {
				return true;
			}
		}

		return false;
	}

	public static Boolean listContains(Collection<String> toCheckIn, String toCheckFor) {
		for(String object : toCheckIn) {
			if(object.equalsIgnoreCase(toCheckFor)) {
				return true;
			}
		}

		return false;
	}

	public static String arrayToString(String[] a, String separator) {
		StringBuilder result = new StringBuilder();
		if (a == null) {
			return null;
		} else if (a.length > 0) {
			result.append(a[0]);
			for (int i = 1; i < a.length; i++) {
				result.append(separator);
				result.append(a[i]);
			}
		}

		return result.toString();
	}

	public static Boolean parseBoolean(String bool) {
		if(bool == null || bool.isEmpty()) {
			return false;
		}

		if(bool.equalsIgnoreCase(Boolean.TRUE.toString())) {
			return true;
		}

		return false;
	}

    public static String parseString(Object value) {
        return value == null ? "" : value.toString();
    }

	public static DateTime xmlGregorianCalendarToDateTime(XMLGregorianCalendar calendar) {
		if(calendar == null) {
			return  null;
		}

		DateTimeZone timeZone = DateTimeZone.forTimeZone(calendar.getTimeZone(0));

		DateTime dateTime = new DateTime(calendar.toGregorianCalendar().getTime(), timeZone);

		return dateTime;
	}

	public static XMLGregorianCalendar dateTimeToXmlGregorianCalendar(DateTime dateTime) {
		if(dateTime == null) {
			return  null;
		}

		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.setTimeInMillis(dateTime.toLocalDateTime().toDate().getTime());

		try {
			return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
		} catch (DatatypeConfigurationException e) {
			return null;
		}
	}

	public static String createEntireInStatement(Collection<String> values) {
		return createEntireInStatement("", values);
	}

	public static String createEntireInStatement(String field, Collection<String> values) {
		String commaSeparated = values.parallelStream().map( value -> {
			return "'" + value + "'";
		}).collect(Collectors.joining(","));

		return field + " IN (" + commaSeparated + ")";
	}

    public static Integer nullCheck(Integer value) {
        return nullCheck(value, 0);
    }

    public static <T> T nullCheck(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static BigDecimal nullCheck(BigDecimal value) {
        return nullCheck(value, BigDecimal.ZERO);
    }

    public static String nullCheck(String value) {
        return nullCheck(value, "");
    }

    public static String longDateFormatToShort(String applicationDateFormat) {
        String shortDateFormat = StringUtils.defaultIfBlank(applicationDateFormat, "").replaceAll("MM", "M").replaceAll("dd", "d").replaceAll("yyyy", "yy");

        return new StringBuilder(shortDateFormat).append(" h:mm a").toString();
    }

    public static String javaDateFormatToJavascriptDateFormat(String format) {
        return StringUtils.defaultIfBlank(format, "").replaceAll("d", "D").replaceAll("y", "Y");
    }

    public static String getHostFromBullhornUrl(String currentBullhornUrl) {
        return currentBullhornUrl.substring(0, StringUtils.indexOfIgnoreCase(currentBullhornUrl, "/bullhornstaffing/"));
    }

    public static <T extends QueryEntity, R> void queryAndProcessAll(Class<T> type, String where, Set<String> fields, Consumer<T> process) {
        queryForAll(type, where, fields, (batch) -> {
            batch.parallelStream().forEach(process);
        });
    }

    public static <T extends QueryEntity, R> List<R> queryAndMapAll(Class<T> type, String where, Set<String> fields, Function<T, R> map) {
        List<R> result = Lists.newArrayList();

        queryForAll(type, where, fields, (batch) -> {
            result.addAll(batch.parallelStream().map(map).collect(Collectors.toList()));
        });

        return result;
    }

    public static <T extends QueryEntity> List<T> queryAndFilterAll(Class<T> type, String where, Set<String> fields, Predicate<T> filter) {
        List<T> result = Lists.newArrayList();

        queryForAll(type, where, fields, (batch) -> {
            result.addAll(batch.parallelStream().filter(filter).collect(Collectors.toList()));
        });

        return result;
    }

    public static <T extends QueryEntity, R> R queryAndCollectAll(Class<T> type, String where, Set<String> fields, Collector<T, R, R> collect) {
        R result = collect.supplier().get();

        queryForAll(type, where, fields, (batch) -> {
            batch.parallelStream().forEach( entity -> {
                collect.accumulator().accept(result, entity);
            });
        });

        return result;
    }

    public static <T extends SearchEntity, R> void searchAndProcessAll(Class<T> type, String where, Set<String> fields, Consumer<T> process) {
        searchForAll(type, where, fields, (batch) -> {
            batch.parallelStream().forEach(process);
        });
    }

    public static <T extends SearchEntity, R> List<R> searchAndMapAll(Class<T> type, String where, Set<String> fields, Function<T, R> map) {
        List<R> result = Lists.newArrayList();

        searchForAll(type, where, fields, (batch) -> {
            result.addAll(batch.parallelStream().map(map).collect(Collectors.toList()));
        });

        return result;
    }

    public static <T extends SearchEntity> List<T> searchAndFilterAll(Class<T> type, String where, Set<String> fields, Predicate<T> filter) {
        List<T> result = Lists.newArrayList();

        searchForAll(type, where, fields, (batch) -> {
            result.addAll(batch.parallelStream().filter(filter).collect(Collectors.toList()));
        });

        return result;
    }

    public static <T extends SearchEntity, R> R searchAndCollectAll(Class<T> type, String where, Set<String> fields, Collector<T, R, R> collect) {
        R result = collect.supplier().get();

        searchForAll(type, where, fields, (batch) -> {
            batch.parallelStream().forEach( entity -> {
                collect.accumulator().accept(result, entity);
            });
        });

        return result;
    }

    private static <T extends QueryEntity> void queryForAll(Class<T> type, String where, Set<String> fields, Consumer<List<T>> process) {
        queryForAll(type, where, fields, process, 0);
    }

    private static <T extends QueryEntity> void queryForAll(Class<T> type, String where, Set<String> fields, Consumer<List<T>> process, Integer start) {
        BullhornData bullhornData = getBullhornData();

        QueryParams params = ParamFactory.queryParams();
        params.setStart(start);
        params.setCount(BATCH_SIZE);
        params.setShowTotalMatched(true);

        ListWrapper<T> result = bullhornData.query(type, where, fields, params);

        process.accept(result.getData());

        if(result.getStart() + result.getCount() > result.getTotal()) {
            queryForAll(type, where, fields, process, result.getStart() + result.getCount());
        }
    }

    private static <T extends SearchEntity> void searchForAll(Class<T> type, String where, Set<String> fields, Consumer<List<T>> process) {
        searchForAll(type, where, fields, process, 0);
    }

    private static <T extends SearchEntity> void searchForAll(Class<T> type, String where, Set<String> fields, Consumer<List<T>> process, Integer start) {
        BullhornData bullhornData = getBullhornData();

        SearchParams params = ParamFactory.searchParams();
        params.setStart(start);
        params.setCount(BATCH_SIZE);

        ListWrapper<T> result = bullhornData.search(type, where, fields, params);

        process.accept(result.getData());

        if(result.getStart() + result.getCount() > result.getTotal()) {
            searchForAll(type, where, fields, process, result.getStart() + result.getCount());
        }
    }

    private static final int BATCH_SIZE = 200;

    private static synchronized BullhornData getBullhornData() {
        if(BULLHORN_DATA == null) {
            BULLHORN_DATA = AppContext.getApplicationContext().getBean(BullhornData.class);
        }

        return BULLHORN_DATA;
    }

    private static BullhornData BULLHORN_DATA;

}
