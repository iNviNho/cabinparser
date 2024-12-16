<template>
    <VueFinalModal
        v-model="openCabinSection"
        class="confirm-modal"
        content-class="confirm-modal-content"
        overlay-transition="vfm-fade"
        content-transition="vfm-fade"
      >
      <div id="opened-cabin">
      <div class="left-section">
        <div class="carousel">
          <h3 class="left-click" @click="previousItem()" v-text="'<'"></h3>
          <div :style="'background-image: url(' + image.image + ')'" :class="['carousel-item', 'item-' + image.key, image.key === this.activeKey ? 'active': null]" v-for="image in selectedCabin.processedImages" :key="image.key"></div>
          <h3 class="right-click" @click="nextItem()" v-text="'>'"></h3>
        </div>
    </div>
    <div class="right-section">
      <h2 class="close" @click="openCabinSection = false">X</h2>
      <h2 class="star" @click="toggleStar(selectedCabin.id)">
        <svg v-if="!selectedCabin.star" xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 16 16">
          <path stroke="black" stroke-width="1" d="M2.866 14.85c-.078.444.36.791.746.593l4.39-2.256 4.389 2.256c.386.198.824-.149.746-.592l-.83-4.73 3.522-3.356c.33-.314.16-.888-.282-.95l-4.898-.696L8.465.792a.513.513 0 0 0-.927 0L5.354 5.12l-4.898.696c-.441.062-.612.636-.283.95l3.523 3.356-.83 4.73zm4.905-2.767-3.686 1.894.694-3.957a.56.56 0 0 0-.163-.505L1.71 6.745l4.052-.576a.53.53 0 0 0 .393-.288L8 2.223l1.847 3.658a.53.53 0 0 0 .393.288l4.052.575-2.906 2.77a.56.56 0 0 0-.163.506l.694 3.957-3.686-1.894a.5.5 0 0 0-.461 0z"/>
        </svg>

        <svg v-if="selectedCabin.star" xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="yellow" viewBox="0 0 16 16">
          <path stroke="black" stroke-width="2" d="M3.612 15.443c-.386.198-.824-.149-.746-.592l.83-4.73L.173 6.765c-.329-.314-.158-.888.283-.95l4.898-.696L7.538.792c.197-.39.73-.39.927 0l2.184 4.327 4.898.696c.441.062.612.636.282.95l-3.522 3.356.83 4.73c.078.443-.36.79-.746.592L8 13.187l-4.389 2.256z"/>
        </svg>
      </h2>
      <h3 style="margin-bottom: 0; margin-top: 0px;">
        <a :href="'https://megaubytovanie.sk/' + selectedCabin.urlFragment" style="color: black;"
           target="_blank">{{ selectedCabin.name }}</a>
      </h3>
      <p style="margin-top: 0;">
        <small>
          {{ selectedCabin.region }} > {{ selectedCabin.district }} > {{ selectedCabin.locality }}
        </small>
      </p>
      <p><strong>Rating:</strong> {{ selectedCabin.rating }} <strong>Hodnotenia:</strong> {{
          selectedCabin.reviewsCount
        }} <strong>Cena za noc:</strong> {{ selectedCabin.avgPricePerNight }}€
        <strong>Obsadenost:</strong> {{ (selectedCabin.occupancy * 100).toFixed(2) }}%</p>
      <p>{{ selectedCabin.description }}</p>
      <p>
        Pocet izieb: {{ selectedCabin.bedroomsCount }}<br>
        Pocet posteli: {{ selectedCabin.regularSleepingBeds }} |
        Pocet extra posteli: {{ selectedCabin.extraSleepingBeds }}
      </p>
      <ul class="attributes">
        <li v-for="attribute in selectedCabin.attributes" :key="attribute" v-text="attribute"></li>
      </ul>
    </div>
    <br style="clear: both;">
    <div style="text-align: right;">
      <select v-model="selectedOption" class="my-chart-select" @change="showChart">
        <option v-for="option in options" :key="option" :value="option" v-text="option"></option>
      </select>
    </div>
    <div class="my-chart">
      <Line :data="chartData" :options="chartOptions"></Line>
    </div>
        </div>
    </VueFinalModal>
</template>

<script>

import {Line} from 'vue-chartjs'
import {
  CategoryScale,
  Chart as ChartJS,
  LinearScale,
  LineController,
  LineElement,
  PointElement,
  Tooltip
} from 'chart.js'
import client from "@/services/client";
import 'vue-final-modal/style.css'
import { VueFinalModal } from 'vue-final-modal'

ChartJS.register(LineController, CategoryScale, LinearScale, PointElement, LineElement, Tooltip)

export default {
  name: 'CabinView',
  components: {
    Line,
    VueFinalModal,
  },
  data() {
    return {
      activeKey: 0,
      openCabinSection: false,
      selectedCabin: null,
      options: [
        'Mesacne',
        'Tyzdenne'
      ],
      selectedOption: 'Mesacne',
      chartData: {
        labels: [],
        datasets: [{data: []}]
      },
      chartOptions: {
        responsive: true,
        maintainAspectRatio: false,
        scales: {
          y: {
            suggestedMin: 0,
            suggestedMax: 100
          }
        },
        plugins: {
          tooltip: {
            enabled: true,
            callbacks: {
              label: function (context) {
                return 'Obsadenost pre ' + context.label + ' je ' + context.raw.toFixed(2) + '%';
              }
            }
          }
        }
      }
    }
  },
  methods: {
    toggleStar(cabinId) {
      client.patch('/cabins/' + cabinId, {
        star: !this.selectedCabin.star
      })
          .then(response => {
            this.selectedCabin.star = response.data.star;
          }).catch(error => {
        console.error(error);
      });
    },
    previousItem() {
      if (this.activeKey === 0) {
        this.activeKey = this.selectedCabin.processedImages.length - 1;
      } else {
        this.activeKey--;
      }
    },
    nextItem() {
      if (this.activeKey === this.selectedCabin.processedImages.length - 1) {
        this.activeKey = 0;
      } else {
        this.activeKey++;
      }
    },
    showCabin(cabin) {
      this.openCabinSection = true;
      this.selectedCabin = cabin;
      this.selectedCabin.processedImages = this.selectedCabin.images.map((image, key) => {
        return {
          image: image,
          key: key
        };
      });
      this.showChart();
      this.loadImagesToCache();
    },
    loadImagesToCache() {
      this.selectedCabin.images.forEach((image) => {
        const img = new Image();
        img.src = image;
      });
    },
    showChart() {
      // create me labels that start like this Jan 2022, Feb 2022, Mar 2022 .. for last 12 months
      client.get('/cabin-occupancy/' + this.selectedCabin.id)
          .then(response => {
            // response structure is like this
            // [
            // {
            //   "id": 262072,
            //   "date": [2024, 10, 31],
            //   "cabin_id": 5687,
            //   "created_at": 1730322972.41378
            // }]
            // we need to group by year and month and count how many times it appears

            let grouped;
            let labels;
            if (this.selectedOption == 'Mesacne') {
              grouped = Object.values(response.data.reduce((acc, item) => {
                const key = item.date[0] + '-' + item.date[1];
                acc[key] = acc[key] ? acc[key] + 1 : 1;
                return acc;
              }, {})).map((value) => {
                return value / 31 * 100; // we assume 31 days in a month;
              });

              // create const labels that just returns unique keys from grouped
              labels = Object.keys(response.data.reduce((acc, item) => {
                const key = item.date[0] + '-' + item.date[1];
                acc[key] = true;
                return acc;
              }, {})).map((key) => {
                return key.split('-').join('-');
              });
            } else {
              // group by REAL weeks
              // i want to have keys getting week start date and end date
              grouped = Object.values(response.data.reduce((acc, item) => {
                const date = new Date(item.date[0], item.date[1] - 1, item.date[2]);
                const weekStart = new Date(date.setDate(date.getDate() - date.getDay()));
                const weekEnd = new Date(date.setDate(date.getDate() - date.getDay() + 6));
                const key = weekStart.toISOString().split('T')[0] + '-' + weekEnd.toISOString().split('T')[0];
                acc[key] = acc[key] ? acc[key] + 1 : 1;
                return acc;
              }, {})).map((value) => {
                return value / 7 * 100; // we assume 7 days in a week;
              });

              labels = Object.keys(response.data.reduce((acc, item) => {
                const date = new Date(item.date[0], item.date[1] - 1, item.date[2]);
                const weekStart = new Date(date.setDate(date.getDate() - date.getDay()));
                const weekEnd = new Date(date.setDate(date.getDate() - date.getDay() + 6));
                const key = weekStart.toISOString().split('T')[0] + '-' + weekEnd.toISOString().split('T')[0];
                acc[key] = true;
                return acc;
              }, {})).map((key) => {
                return key.split('-').join('-');
              });
            }


            this.chartData = {
              labels: labels,
              datasets: [{
                label: 'Obsadenost',
                data: grouped,
                borderColor: 'rgb(75, 192, 192)',
                tension: 0.1,
                pointStyle: 'circle',
                pointRadius: 10,
                pointHoverRadius: 15
              }]
            };
          }).catch(error => {
        console.error(error);
      });
    }
  }
}
</script>

<style>

#opened-cabin .left-section {
  float: left;
  width: 40%;
  overflow: hidden;
}

#opened-cabin .right-section {
  position: relative;
  float: right;
  width: calc(60% - 30px);
  text-align: left;
  padding-left: 15px;
  padding-right: 15px;
}

#opened-cabin .right-section h2.close {
  position: absolute;
  top: 0px;
  right: 0px;
  cursor: pointer;
  text-align: right;
  margin-top: 0px;
}

#opened-cabin .right-section h2.star {
  position: absolute;
  top: 0px;
  right: 25px;
  cursor: pointer;
  text-align: right;
  margin-top: 0px;
}

.slide {
  width: 100%;
  background-size: cover;
  background-position: center;
  height: 400px;
}

.attributes {
  list-style: none;
  padding: 0;
}

.attributes li {
  display: inline-block;
  margin-right: 5px;
  margin-top: 5px;
  background-color: darkcyan;
  color: white;
  padding: 5px;
  border-radius: 5px;
}

.my-chart {
  height: 250px;
}

.my-chart-select {
  margin-bottom: 20px;
  margin-top: 20px;
  padding: 5px;
  border-radius: 5px;
  background-color: white;
  font-size: 16px;
  cursor: pointer;
}

.confirm-modal {
  display: flex;
  justify-content: center;
  align-items: center;
}

.confirm-modal-content {
  width: 80%;
  max-height: 80%;
  display: flex;
  flex-direction: column;
  padding: 1rem;
  background: #fff;
  border-radius: 0.5rem;
  overflow: scroll;
}
.confirm-modal-content > * + *{
  margin: 0.5rem 0;
}
.confirm-modal-content h1 {
  font-size: 1.375rem;
}
.confirm-modal-content button {
  margin: 0.25rem 0 0 auto;
  padding: 0 8px;
  border: 1px solid;
  border-radius: 0.5rem;
}
.dark .confirm-modal-content {
  background: #000;
}

.carousel {
  position: relative;
  width: 100%;
  height: 400px;
  overflow: hidden;
}

.carousel-item {
  width: 100%;
  height: 400px;
  background-size: cover;
  background-position: center;
  display: none;
}

.carousel-item.active {
  display: block;
}

.carousel .left-click {
  position: absolute;
  top: 50%;
  left: 0;
  cursor: pointer;
  font-size: 40px;
  color: white;
  transform: translateY(-50%);
  margin: 0;
  padding: 0 10px;
  background-color: rgba(0, 0, 0, 0.5);
  border-top-right-radius: 10px;
  border-bottom-right-radius: 10px;
}

.carousel .right-click {
  position: absolute;
  top: 50%;
  right: 0;
  cursor: pointer;
  font-size: 40px;
  color: white;
  margin: 0;
  padding: 0 10px;
  transform: translateY(-50%);
  background-color: rgba(0, 0, 0, 0.5);
  border-top-left-radius: 10px;
  border-bottom-left-radius: 10px;
}

</style>