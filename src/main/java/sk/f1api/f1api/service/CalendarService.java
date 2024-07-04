package sk.f1api.f1api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.Setter;
import sk.f1api.f1api.model.CalendarModel;

@Service
@Setter
@Getter
public class CalendarService {

  private List<CalendarModel> calendars;

  public CalendarService() {
    
  }

  public void loadAll() {
    this.calendars = CalendarModel.loadAll();
  }
}
