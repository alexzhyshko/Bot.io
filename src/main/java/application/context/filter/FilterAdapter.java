package application.context.filter;

import org.telegram.telegrambots.meta.api.objects.Update;

import application.exception.FilterException;

public interface FilterAdapter {
	public Update filter(Update update) throws FilterException;
}
