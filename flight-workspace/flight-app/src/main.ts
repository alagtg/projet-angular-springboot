import { bootstrapApplication } from '@angular/platform-browser';
import { provideHttpClient } from '@angular/common/http';   // ⚡ import obligatoire
import { AppComponent } from './app/app.component';
import { appConfig } from './app/app.config';               // ⚡ si tu as un app.config

bootstrapApplication(AppComponent, {
  providers: [
    provideHttpClient(),
    ...appConfig.providers || []   // ⚡ garde aussi tes autres providers
  ]
}).catch((err) => console.error(err));
