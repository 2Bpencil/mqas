package com.tyf.mqas.base.page;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 查询条件绑定
 */
@SuppressWarnings({ "rawtypes", "unchecked", "incomplete-switch" })
public class SearchUtils {

	/**
	 * 查询条件格式 List<SearchFilter> filterList = new LinkedList<SearchFilter>(); filterList.add(new
	 * SearchFilter("name",SearchFilter.Operator.EQ,"小白白"));
	 */
	public static String filterWhereForPage(List<SearchFilter> filterList) {

		StringBuilder sb = new StringBuilder();
		for (Iterator<SearchFilter> it = filterList.iterator(); it.hasNext();) {
			SearchFilter filter = it.next();
			if (StringUtils.isBlank(filter.value == null ? null : filter.value.toString()))
				continue;
			switch (filter.operator) {
			case EQ:
				sb.append(filter.fieldName).append("=").append("'" + filter.value + "'");
				break;
			case NEQ:
				sb.append(filter.fieldName).append("!=").append("'" + filter.value + "'");
				break;
			case LIKE:
				sb.append(filter.fieldName).append(" like ").append("'%" + filter.value + "%'");
				break;
			case GT:
				sb.append(filter.fieldName).append(">").append("'" + filter.value + "'");
				break;
			case LT:
				sb.append(filter.fieldName).append("<").append("'" + filter.value + "'");
				break;
			case GTE:
				sb.append(filter.fieldName).append(">=").append("'" + filter.value + "'");
				break;
			case LTE:
				sb.append(filter.fieldName).append("<=").append("'" + filter.value + "'");
				break;
			case IS:
				sb.append(filter.fieldName).append(" is ").append(filter.value + "");
				break;
			case OR:
				sb.append(" or ");
				sb.append(filter.fieldName).append("=").append("'" + filter.value + "'");
				break;
			case IN: {
				sb.append(filter.fieldName).append(" in ").append("(");
				String[] split = filter.value.toString().split(",");
				for (int i = 0; i < split.length - 1; i++) {
					String value = split[i];
					sb.append("'" + value + "',");
				}
				sb.append("'" + split[split.length - 1] + "')");

			}
				// sb.append(filter.fieldName).append(" in ").append("("+filter.value+")");
				break;
			case NotIn:
				sb.append(filter.fieldName).append(" not in ").append("(" + filter.value + ")");
				break;
			case INSTR: // instr(customId,'sss')>0

				String[] split = filter.value.toString().split(",");
				sb.append(" (");
				for (int i = 0; i < split.length; i++) {
					String value = split[i];
					sb.append(" instr(").append(filter.fieldName).append(",").append("'" + value + "')>0");
					if (i != split.length - 1) {
						sb.append(" or ");
					}
				}
				// sb.append("
				// instr(").append(filter.fieldName).append(",").append("'"+filter.value+"')>0");
				sb.append(")");
				break;

			}
			sb.append(" and ");
		}
		if (sb.toString().endsWith("and ")) {
			return sb.substring(0, sb.length() - 4);
		}
		return sb.toString();
	}

	/**
	 * 排序条件 List<SearchFilter> filterList = new LinkedList<SearchFilter>(); filterList.add(new
	 * SearchFilter("name",SearchFilter.Operator.EQ,"小白白"));
	 * 在sortname和sortorder中进行排序时，格式为--sortname:排序字段1#排序字段2,sortorder:如果为一个则排序字段共用，如果为asc#desc则必须对应排序字段
	 */
	public static String filterOrderForPage(List<SearchFilter> filterList) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String sortname = request.getParameter("sidx");
		String sortorder = request.getParameter("sord");
		StringBuilder sb = new StringBuilder(" order by ");

		if (StringUtils.isNotBlank(sortname)) {
			if (sortname.indexOf("#") != -1) {
				String[] sortNameStrings = sortname.split("#");
				String Str = "";
				if (sortorder.indexOf("#") != -1) {
					String[] sortorderString = sortorder.split("#");
					for (int i = 0; i < sortNameStrings.length; i++) {
						Str += "," + camel4underline(sortNameStrings[i]) + " " + sortorderString[i];
					}
					sb.append(Str.substring(1));
				} else {
					for (int i = 0; i < sortNameStrings.length; i++) {
						Str += "," + camel4underline(sortNameStrings[i]) + " " + sortorder;
					}
					sb.append(Str.substring(1));
				}
			} else {
				sb.append(camel4underline(sortname)).append(" ").append(sortorder);
			}
		} else {
			for (Iterator<SearchFilter> it = filterList.iterator(); it.hasNext();) {
				SearchFilter filter = it.next();
				switch (filter.operator) {
				case ASC:
					sb.append(camel4underline(filter.fieldName)).append(" asc, ");
					break;
				case DESC:
					sb.append(camel4underline(filter.fieldName)).append(" desc, ");
					break;
				}
			}
		}
		if (sb.length() == 10)
			return null;
		if (sb.toString().endsWith(", "))
			return sb.substring(0, sb.length() - 2);
		return sb.toString();
	}

	/**
	 * 排序条件 List<SearchFilter> filterList = new LinkedList<SearchFilter>(); filterList.add(new
	 * SearchFilter("name",SearchFilter.Operator.EQ,"小白白"));
	 * 在sortname和sortorder中进行排序时，格式为--sortname:排序字段1#排序字段2,sortorder:如果为一个则排序字段共用，如果为asc#desc则必须对应排序字段
	 */
	public static String filterOrderForPageSql(List<SearchFilter> filterList) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String sortname = request.getParameter("sidx");
		String sortorder = request.getParameter("sord");
		StringBuilder sb = new StringBuilder(" order by ");

		if (StringUtils.isNotBlank(sortname)) {
			if (sortname.indexOf("#") != -1) {
				String[] sortNameStrings = sortname.split("#");
				String Str = "";
				if (sortorder.indexOf("#") != -1) {
					String[] sortorderString = sortorder.split("#");
					for (int i = 0; i < sortNameStrings.length; i++) {
						Str += "," + sortNameStrings[i] + " " + sortorderString[i];
					}
					sb.append(Str.substring(1));
				} else {
					for (int i = 0; i < sortNameStrings.length; i++) {
						Str += "," + sortNameStrings[i] + " " + sortorder;
					}
					sb.append(Str.substring(1));
				}
			} else {
				sb.append(sortname).append(" ").append(sortorder);
			}
		} else {
			for (Iterator<SearchFilter> it = filterList.iterator(); it.hasNext();) {
				SearchFilter filter = it.next();
				switch (filter.operator) {
				case ASC:
					sb.append(filter.fieldName).append(" asc, ");
					break;
				case DESC:
					sb.append(filter.fieldName).append(" desc, ");
					break;
				}
			}
		}
		if (sb.length() == 10)
			return null;
		if (sb.toString().endsWith(", "))
			return sb.substring(0, sb.length() - 2);
		return sb.toString();
	}

	/**
	 * 取得带相同前缀的Request Parameters, copy from spring WebUtils. 返回的结果的Parameter名已去除前缀.
	 * 条件查询--前台参数格式"search#like#it":1
	 * 参数中添加排序--"condition":"[{field:'r_createtime',op:'asc',value:''},{field:'r_createtime',op:'asc',value:''}]"
	 */
	public static List<SearchFilter> getParametersStartingWith(ServletRequest request, String prefix) {
		Validate.notNull(request, "Request must not be null");
		List<SearchFilter> filterList = new LinkedList<SearchFilter>();
		Enumeration paramNames = request.getParameterNames();
		if (prefix == null) {
			prefix = "";
		}
		while (paramNames != null && paramNames.hasMoreElements()) {
			String paramName = (String) paramNames.nextElement();
			if (paramName.startsWith(prefix)) {
				String unprefixed = paramName.substring(prefix.length() + 1);
				String[] values = request.getParameterValues(paramName);
				if (values.length > 0 && StringUtils.isNotBlank(values[0]))
					if (values == null || values.length == 0) {
						// Do nothing, no values found at all.
					} else {
						String operator = unprefixed.split("#")[0];
						String name = unprefixed.replace(operator + "#", "");
						for (SearchFilter.Operator oper : SearchFilter.Operator.values()) {
							if (oper.name().equalsIgnoreCase(operator)) {
								filterList.add(new SearchFilter(name, oper, values[0]));
								break;
							}

						}
					}
			}
		}
		String search = request.getParameter("condition");
		if (StringUtils.isBlank(search)) {
			return filterList;
		}
		JSONArray jsonArray = JSONArray.fromObject(search);
		Iterator<JSONObject> it = jsonArray.iterator();
		while (it.hasNext()) {
			JSONObject jsonObject = it.next();
			String name = jsonObject.get("field").toString();
			String operator = jsonObject.get("op").toString();
			String value = jsonObject.get("value").toString();
			for (SearchFilter.Operator oper : SearchFilter.Operator.values()) {
				if (oper.name().equalsIgnoreCase(operator))
					filterList.add(new SearchFilter(name, oper, value));
			}

		}
		return filterList;
	}

	public static String camel4underline(String param) {
		Pattern p = Pattern.compile("[A-Z]");
		if (param == null || param.equals("")) {
			return "";
		}
		StringBuilder builder = new StringBuilder(param);
		Matcher mc = p.matcher(param);
		int i = 0;
		while (mc.find()) {
			builder.replace(mc.start() + i, mc.end() + i, "_" + mc.group().toLowerCase());
			i++;
		}

		if ('_' == builder.charAt(0)) {
			builder.deleteCharAt(0);
		}
		return builder.toString();
	}

}
