import { Component } from "@angular/core"
import { CommonModule } from "@angular/common"
import { FormsModule } from "@angular/forms"
import { HttpClientModule } from "@angular/common/http"

import { SearchFormComponent } from "./components1/search-form.component"
import { VolListComponent } from "./components2/vol-list.component"

// Import du service
import { VolService, Vol } from "./services/vol.service"

@Component({
  selector: "app-root",
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule, SearchFormComponent, VolListComponent],
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.scss"],
})
export class AppComponent {
  vols: Vol[] = []
  loading = false
  errorMessage = ""

  constructor(private volService: VolService) {}

  rechercher(filters: any) {
    this.loading = true
    this.errorMessage = ""

    this.volService.getVols(filters).subscribe({
      next: (data: Vol[]) => {
        this.vols = data
        this.loading = false
        console.log("✅ Vols reçus:", data)
      },
      error: (err) => {
        this.errorMessage = "Erreur lors du chargement des vols."
        this.loading = false
        console.error("❌ Erreur API:", err)
      },
    })
  }
}
