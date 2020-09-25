package application.context.filter;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.meta.api.objects.Update;

import application.context.ApplicationContext;
import application.context.annotation.Filter;
import application.exception.FilterException;

public class FilterContext {

	private FilterContext() {
	}

	private static List<Class> filters = new ArrayList<>();

	public static void addFilter(Class filterClass) {
		int filterOrder = ((Filter) filterClass.getDeclaredAnnotation(Filter.class)).order();
		filters.add(filterOrder, filterClass);
	}

	public static boolean filter(Update update) {
		try {
			for (Class filter : filters) {
				boolean enabled = ((Filter) filter.getDeclaredAnnotation(Filter.class)).enabled();
				if (enabled) {
					FilterAdapter filterInstance = (FilterAdapter) ApplicationContext.getComponent(filter);
					update = filterInstance.filter(update);
				}
			}
		} catch (FilterException e) {
			return false;
		}
		return true;
	}
}
