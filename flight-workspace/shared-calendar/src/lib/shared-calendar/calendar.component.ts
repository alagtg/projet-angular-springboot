import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'lib-calendar',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.scss']
})
export class CalendarComponent {
  currentDate = new Date();
  weeks: (Date | null)[][] = [];
  selectedDate: Date | null = null;

  @Output() dateSelected = new EventEmitter<string>();

  constructor() {
    this.generateCalendar();
  }

  generateCalendar() {
    const start = new Date(this.currentDate.getFullYear(), this.currentDate.getMonth(), 1);
    const end = new Date(this.currentDate.getFullYear(), this.currentDate.getMonth() + 1, 0);

    this.weeks = [];
    let week: (Date | null)[] = [];

    for (let i = 0; i < start.getDay(); i++) {
      week.push(null);
    }

    for (let d = 1; d <= end.getDate(); d++) {
      week.push(new Date(this.currentDate.getFullYear(), this.currentDate.getMonth(), d));
      if (week.length === 7) {
        this.weeks.push(week);
        week = [];
      }
    }

    if (week.length > 0) this.weeks.push(week);
  }

  selectDate(date: Date) {
    this.selectedDate = date;

    const localDate = date.toLocaleDateString('fr-CA'); // format YYYY-MM-DD
    this.dateSelected.emit(localDate);
  }

  prevMonth() {
    this.currentDate = new Date(this.currentDate.getFullYear(), this.currentDate.getMonth() - 1, 1);
    this.generateCalendar();
  }

  nextMonth() {
    this.currentDate = new Date(this.currentDate.getFullYear(), this.currentDate.getMonth() + 1, 1);
    this.generateCalendar();
  }
}
