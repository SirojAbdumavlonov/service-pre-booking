import axios from 'axios'

const base_url = 'http://localhost:8080/'

class CategoryService {
    getCategories(){
        return axios.get(base_url)
    }
    getCategory(id){
        return axios.get(base_url + `${id}`)
    }
}

export default new CategoryService()