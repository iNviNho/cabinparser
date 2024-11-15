import {createApp} from 'vue/dist/vue.esm-bundler'
import App from './App.vue'
import VueGoogleMaps from '@fawmi/vue-google-maps'
import VueCarousel from '@chenfengyuan/vue-carousel'
import {googleMapsConfig} from "@/config/config";

const app = createApp(App);
app.component(VueCarousel.name, VueCarousel);
app.use(VueGoogleMaps, {
    load: {
        key: googleMapsConfig.apiKey,
    },
}).mount('#app')
