<template id="employee-overview">
  <app-layout>
    <!-- Card - for adding a new user -->
    <div class="card bg-light mb-3">
      <div class="card-header">
        <div class="row">
          <div class="col-6">
            Users
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

        <form id="addUser">

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-id">Id</span>
            </div>
            <input type="number" class="form-control" v-model="formData.id" name="id" placeholder="Id"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-e_name">Name</span>
            </div>
            <input type="text" class="form-control" v-model="formData.e_name" name="name" placeholder="Name"/>
          </div>


          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-e_phone">Phone</span>
            </div>
            <input type="number" class="form-control" v-model="formData.e_phone" name="e_phone" placeholder="Phone"/>
          </div>


          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-designation">Designation</span>
            </div>
            <input type="text" class="form-control" v-model="formData.designation" name="designation" placeholder="Designation"/>
          </div>
</form>

        <button rel="tooltip" title="Insert" class="btn btn-info btn-simple btn-link" @click="addUser()">Add User</button>
      </div>
    </div>

    <!-- List Group - displays all the users -->
    <div class="list-group list-group-flush">
      <table>
        <tr>
          <th>&nbsp &nbsp &nbsp</th>
          <th>Id </th>
          <th> &nbsp&nbsp &nbsp &nbsp &nbsp &nbsp&nbsp &nbsp &nbsp &nbsp </th>
          <th>Name </th>
          <th>&nbsp &nbsp &nbsp &nbsp &nbsp &nbsp </th>
          <th>Phone  </th>
          <th> &nbsp &nbsp &nbsp&nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp </th>
          <th>Designation  </th>
        </tr>
      </table>
      <div class="list-group-item d-flex align-items-start"
           v-for="(user,index) in users" v-bind:key="index">
        <div class="mr-auto p-2">
          <table style="width: 50px">
            <tr > {{ user.id }}</tr>
            <tr>&nbsp &nbsp  &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp&nbsp &nbsp   </tr>

            <tr > {{ user.e_name }}</tr>
            <tr>&nbsp &nbsp   &nbsp &nbsp &nbsp&nbsp &nbsp &nbsp &nbsp </tr>

            <tr > {{ user.e_phone }}</tr>
            <tr> &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp </tr>
            <tr> {{ user.designation }}</tr>

          </table>
        </div>



        <div class="p2">
          <a :href="`/employees/${user.id}`">
            <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link">
              <i class="fa fa-pencil" aria-hidden="true"></i>
            </button>
          </a>
          <button rel="tooltip" title="Delete" class="btn btn-info btn-simple btn-link"
                  @click="deleteUser(user, index)">
            <i class="fas fa-trash" aria-hidden="true"></i>
          </button>
        </div>
      </div>
    </div>
  </app-layout>
</template>



<script>
Vue.component("employee-overview", {
  template: "#employee-overview",
  data: () => ({
    users: [],
    formData: [],
    hideForm :true,
  }),
  created() {
    this.fetchUsers();
  },
  methods: {
    fetchUsers: function () {
      axios.get("/api/employees")
          .then(res => this.users = res.data)
          .catch(() => alert("Error while fetching users"));
    },
    deleteUser: function (user, index) {
      if (confirm('Are you sure you want to delete this user? This action cannot be undone.', 'Warning')) {
        //user confirmed delete
        const userId = user.id;
        const url = `/api/employees/${userId}`;
        axios.delete(url)
            .then(response =>
                //delete from the local state so Vue will reload list automatically
                this.users.splice(index, 1).push(response.data))
            .catch(function (error) {
              console.log(error)
            });
      }
    },
    addUser: function (){
      const url = `/api/employees`;
      axios.post(url,
          {
            id: this.formData.id,
            e_name: this.formData.e_name,
            e_phone: this.formData.e_phone,
            designation: this.formData.designation,

          })
          .then(response => {
            this.users.push(response.data)
            this.hideForm= true;
          })
          .catch(error => {
            console.log(error)
          })
    }
  }
});
</script>
