<template>

    <div class="sides">
      <div v-if="!categoryPage" class="left-side">
        <div class="together">
          <input type="checkbox" id="service" class="left-side-opt">
          <label for="service">NEW SERVICES</label>
        </div>
        <div class="together">
        <input type="checkbox" class="left-side-opt">
          <label> new prices</label>
        </div>
        <div class="together">
          <input type="checkbox" class="left-side-opt">
          <label>old prices</label>
        </div>
        <div class="together">
          <input type="checkbox" class="left-side-opt">
          <label>old prices</label>
        </div>
        <br>
        <div class="together">
          <h3>Price</h3>
        </div>

        <div class="together">
          <div class="range">
              <input type="number" >
              <input type="number" >
          </div>
        </div>
      </div>
      <div class="right-side">
        <div class="py-3 py-md-5 bg-light">
          <div class="container">
            <div class="row">
              <div class="col-md-12">
                <h4 class="mb-4" style="margin: auto">Our Categories</h4>
              </div>
              <div class="col-6 col-md-3" v-for="category in categories" :key="category.id">
                <div class="category-card">
                  <a :href="category.id + '/services'">
                    <div class="category-card-img">
                      <img :src="'img/category/' + category.categoryImageName" class="w-100" :alt="category.title">
                    </div>
                    <div class="category-card-body">
                      <h5>{{ category.title }}</h5>
                    </div>
                  </a>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
</template>

<script>
  import categoryService from "../services/categoryService.js";

  export default {
    name:'mainPage',
    data(){
      return {
        categories: [],
        categoryPage: true
      }
    },
    methods:{
      getCategories(){
        categoryService.getCategories()
            .then((response) =>{
          this.categories = response.data;
        })
            .catch(e =>{
              alert(e)
            })
      }
    },
    mounted() {
      this.getCategories()
    }

  }


</script>








<style>
.range{
  display: flex;
  margin: 20px;
  justify-content: space-around;
}

input[type="number"]{
  background-color: #5b5b65;
  border-radius: 10px;
  width: 45%;
  height: 40px;
  text-align: center;
}

input[type="checkbox"]{
  appearance: auto;
  background-color: white;
  width: 20px;
  height: 20px;
  border-radius: 30%;
  margin: 5px;

}
input[type="checkbox"]:checked{
  background-clip: content-box;
  background-color: #8c9e8f;
}
.together{
  display: flex;
  text-align: center;
  justify-content: center;
  margin-top: 20px;
}
.left-side-opt{
  margin: 10px;
  width: inherit;
  text-align: center;
  padding: 5px;
}
.sides{
  display: flex;
  width: 100%;
  height: 100%;
}
.left-side{
  width: 22%;
  height: 100%;
  display: flex;
  flex-direction: column;
  background-color: #a7a7c2;
  margin: auto;
}
.right-side{
  width: 77%;
  height: 100%;
}
.mb-4{
  text-align: center;
}

.category-card{
  border: 1px solid #ddd;
  box-shadow: 0 0.125rem 0.25rem rgb(0 0 0 / 8%);
  margin-bottom: 24px;
  background-color: #fff;
}
.category-card a{
  text-decoration: none;
}
.category-card .category-card-img{
  max-height: 260px;
  overflow: hidden;
  border-bottom: 1px solid #ccc;
}
.category-card .category-card-body{
  padding: 10px 16px;
}
.category-card .category-card-body h5{
  margin-bottom: 10px;
  font-size: 18px;
  font-weight: 600;
  color: #000;
  text-align: center;
}

</style>