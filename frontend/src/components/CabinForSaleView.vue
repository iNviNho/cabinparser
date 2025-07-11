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
          <div v-for="image in selectedCabin.processedImages" :style="'background-image: url(' + image.image + ')'" :class="['carousel-item', 'item-' + image.key, image.key === this.activeKey ? 'active': null]" :key="image.key"></div>
          <h3 class="right-click" @click="nextItem()" v-text="'>'"></h3>
        </div>
    </div>
    <div class="right-section">
      <h2 class="close" @click="openCabinSection = false">X</h2>
      <h3 style="margin-bottom: 0; margin-top: 0px;">
        <a :href="selectedCabin.link" style="color: black;"
           target="_blank">{{ selectedCabin.title }}</a>
      </h3>
      <p style="margin-top: 0;">
        <small>
          {{ selectedCabin.region }} > {{ selectedCabin.district }} > {{ selectedCabin.locality }}
        </small>
      </p>
      <p>
        <strong>Pozemok:</strong> {{ selectedCabin.estate }}m²
        <strong>Zastavaná plocha:</strong> {{ selectedCabin.houseEstate }}m²
        <strong>Úžitková plocha:</strong> {{ selectedCabin.floorEstate }}m²
        <strong>Cena:</strong> {{ selectedCabin.price }}€
      </p>
      <p v-html="selectedCabin.description" class="description-section"></p>
    </div>
    <br style="clear: both;">
      </div>
    </VueFinalModal>
</template>

<script>

import 'vue-final-modal/style.css'
import { VueFinalModal } from 'vue-final-modal'

export default {
  name: 'CabinView',
  components: {
    VueFinalModal,
  },
  data() {
    return {
      activeKey: 0,
      openCabinSection: false,
      selectedCabin: null,
    }
  },
  methods: {
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
    showCabinForSale(cabinForSale) {
      this.openCabinSection = true;
      this.selectedCabin = cabinForSale;
      this.selectedCabin.processedImages = this.selectedCabin.images.map((image, key) => {
        return {
          image: image,
          key: key
        };
      });
      this.loadImagesToCache();
    },
    loadImagesToCache() {
      this.selectedCabin.images.forEach((image) => {
        const img = new Image();
        img.src = image;
      });
      this.activeKey = 0;
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

.description-section {
  height: 400px;
  overflow: scroll;
}

</style>