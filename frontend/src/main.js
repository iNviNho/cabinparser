import {createApp} from 'vue/dist/vue.esm-bundler'
import App from './App.vue'
import VueCarousel from '@chenfengyuan/vue-carousel'

const app = createApp(App);
app.component(VueCarousel.name, VueCarousel);
app.mount('#app');