import { Component, Output, EventEmitter, HostListener } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule } from "@angular/forms";
import { CalendarComponent } from "@flight-workspace/shared-calendar";

@Component({
  selector: "app-search-form",
  standalone: true,
  imports: [CommonModule, FormsModule, CalendarComponent],
  templateUrl: "./search-form.component.html",
  styleUrls: ["./search-form.component.scss"],
})
export class SearchFormComponent {
  @Output() search = new EventEmitter<any>();

  villeDepart = "";
  villeArrivee = "";
  dateDepart = "";
  dateArrivee = "";
  tri = "prix";

  showDepartureCalendar = false;
  showReturnCalendar = false;

  toggleDepartureCalendar() {
    this.showDepartureCalendar = !this.showDepartureCalendar;
    this.showReturnCalendar = false;
  }

  toggleReturnCalendar() {
    this.showReturnCalendar = !this.showReturnCalendar;
    this.showDepartureCalendar = false;
  }

  onDepartureDateSelected(date: string) {
    this.dateDepart = date;
    this.showDepartureCalendar = false;
  }

  onReturnDateSelected(date: string) {
    this.dateArrivee = date;
    this.showReturnCalendar = false;
  }

  swapCities() {
    const temp = this.villeDepart;
    this.villeDepart = this.villeArrivee;
    this.villeArrivee = temp;
  }

  onSubmit() {
    const filters = {
      villeDepart: this.villeDepart,
      villeArrivee: this.villeArrivee,
      dateDepart: this.dateDepart,
      dateArrivee: this.dateArrivee,
      tri: this.tri,
    };

    this.search.emit(filters);

    //  Scroll vers les résultats avec effet highlight
    setTimeout(() => {
      const results = document.getElementById("results-section");
      if (results) {
        results.scrollIntoView({ behavior: "smooth", block: "start" });
        results.classList.add("highlight");
        setTimeout(() => results.classList.remove("highlight"), 1500);
      }
    }, 200);
  }

  //  Fermer les calendriers si clic à l’extérieur
  @HostListener("document:click", ["$event"])
  clickOutside(event: Event) {
    const target = event.target as HTMLElement;
    if (!target.closest(".date-wrapper")) {
      this.showDepartureCalendar = false;
      this.showReturnCalendar = false;
    }
  }
}
