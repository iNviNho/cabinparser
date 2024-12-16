import {createApp} from 'vue/dist/vue.esm-bundler'
import App from './App.vue'
import { createVfm } from 'vue-final-modal'

const app = createApp(App);
const vfm = createVfm()

app
    .use(vfm)
    .mount('#app');