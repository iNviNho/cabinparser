<template>
  <div v-if="openCabinSection" id="opened-cabin">
    <div class="left-section">
      <vue-carousel :data="selectedCabin.processedImages"></vue-carousel>
    </div>
    <div class="right-section">
      <h2 id="close" @click="openCabinSection = false">X</h2>
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

ChartJS.register(LineController, CategoryScale, LinearScale, PointElement, LineElement, Tooltip)

export default {
  name: 'CabinView',
  components: {
    Line
  },
  data() {
    return {
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
    showCabin(cabin) {
      this.openCabinSection = true;
      this.selectedCabin = cabin;
      this.selectedCabin.processedImages = this.selectedCabin.images.map(image => {
        return "<div class='slide' style='background-image: url(" + image + ")'></div>";
      });
      this.showChart();
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

#opened-cabin {
  margin-bottom: 20px;
  padding: 0 20px;
  border: 3px solid darkcyan;
  width: 97%;
  border-radius: 5px;
}

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

#opened-cabin .right-section h2 {
  position: absolute;
  top: 0px;
  right: 0px;
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
  height: 400px;
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

</style>