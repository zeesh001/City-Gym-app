<template id="promotion-overview">
  <app-layout>
    <!-- Card - for adding a new user -->
    <div class="card bg-light mb-3">
      <div class="card-header">
        <div class="row">
          <div class="col-6">
            Promotion
          </div>
          <div class="col" align="right">
            <button rel="tooltip" title="Add"
                    class="btn btn-info btn-simple btn-link"
                    @click="hideForm =!hideForm">
              <i class="fa fa-plus" aria-hidden="true"></i>
            </button>
          </div>
        </div>
      </div>
      <div class="card-body" :class="{ 'd-none': hideForm}">
        <form id="addPromotion">
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-id">Id</span>
            </div>
            <input type="number" class="form-control" v-model="formData.id" name="id" placeholder="Id"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-package_cat">Package Type</span>
            </div>
            <input type="text" class="form-control" v-model="formData.package_cat" name="package_cat" placeholder="Package Type"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-service_name">Service Name</span>
            </div>
            <input type="text" class="form-control" v-model="formData.service_name" name="service_name" placeholder="Service Name"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-discount">Discount</span>
            </div>
            <input type="text" class="form-control" v-model="formData.discount" name="discount" placeholder="Discount"/>
          </div>
        </form>
        <button rel="tooltip" title="Add" class="btn btn-info btn-simple btn-link" @click="addPromotion()">Add Promotion</button>
      </div>
    </div>

    <!-- List Group - displays all the users -->
    <div class="list-group list-group-flush">
      <table>
        <tr>
          <th>&nbsp &nbsp &nbsp </th>
          <th>Id </th>
          <th> &nbsp &nbsp  &nbsp  &nbsp &nbsp &nbsp <span>|</span> &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp</th>
          <th>Package Type </th>
          <th> &nbsp &nbsp  &nbsp  &nbsp &nbsp &nbsp <span>|</span> &nbsp &nbsp  &nbsp &nbsp &nbsp &nbsp</th>
          <th>Service Name  </th>
          <th> &nbsp &nbsp  &nbsp &nbsp &nbsp &nbsp <span>|</span> &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp</th>
          <th>Discount  </th>
        </tr>
      </table>
      <div class="list-group-item d-flex align-items-start"
           v-for="(promotion,index) in promotions" v-bind:key="index">
        <div class="mr-auto p-2">
          <table style="width: 50px">
            <tr > {{ promotion.id }}</tr>
            <tr>  &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp </tr>
            <tr > {{ promotion.package_cat }}</tr>
            <tr>  &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp </tr>
            <tr > {{ promotion.service_name }}</tr>
            <tr>  &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp </tr>
            <tr> {{ promotion.discount }}</tr>
          </table>
        </div>

        <div class="p2">
          <!--          <a :href="`/services/${service.service_name}/services`">-->
          <!--            <button rel="tooltip" title="Review" class="btn btn-info btn-simple btn-link">-->
          <!--              <i class="far fa-address-book"></i>-->
          <!--            </button>-->
          <!--          </a>-->

          <!--          <a :href="`/promotions/${promotion.id}`">-->
          <!--            <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link">-->
          <!--              <i class="fa fa-pencil" aria-hidden="true"></i>-->
          <!--            </button>-->
          <!--          </a>-->

          <button rel="tooltip" title="Delete" class="btn btn-info btn-simple btn-link"
                  @click="deleteUser(promotion, index)">
            <i class="fas fa-trash" aria-hidden="true"></i>
          </button>
        </div>
      </div>
    </div>
  </app-layout>
</template>

<script>
Vue.component("promotion-overview", {
  template: "#promotion-overview",
  data: () => ({
    promotions: [],
    formData: [],
    hideForm :true,
  }),
  created() {
    this.fetchUsers();
  },
  methods: {
    fetchUsers: function () {
      axios.get("/api/promotions")
          .then(res => this.promotions = res.data)
          .catch(() => alert("Error while fetching users"));
    },
    deleteUser: function (promotion, index) {
      if (confirm('Are you sure you want to delete this ? This action cannot be undone.', 'Warning')) {
        //user confirmed delete
        const serv = promotion.id;
        const url = `/api/promotions/${serv}`;
        axios.delete(url)
            .then(response =>
                //delete from the local state so Vue will reload list automatically
                this.promotions.splice(index, 1).push(response.data))
            .catch(function (error) {
              console.log(error)
            });
      }
    },
    addPromotion: function (){
      const url = `/api/promotions`;
      axios.post(url,
          {
            id: this.formData.id,
            package_cat: this.formData.package_cat,
            service_name: this.formData.service_name,
            discount: this.formData.discount

          })
          .then(response => {
            this.promotions.push(response.data)
            this.hideForm= true;
          })
          .catch(error => {
            console.log(error)
          })
    }
  }
});
</script>