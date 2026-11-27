package vn.rikkei.cinemabox.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.rikkei.cinemabox.common.DuplicateException;
import vn.rikkei.cinemabox.common.ResourceNotFoundException;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    public CustomerResponse create(CreateCustomerRequest request) {
        String email = request.email().trim();
        if (customerRepository.existsByEmail(email)) {
            throw new DuplicateException("Email đã tồn tại");
        }

        Customer customer = new Customer();
        customer.setFullName(request.fullName().trim());
        customer.setEmail(email);
        customer.setPhone(request.phone());
        customer.setMemberLevel(request.memberLevel() == null
                ? "STANDARD"
                : request.memberLevel());

        return CustomerResponse.from(customerRepository.save(customer));
    }

    @Transactional(readOnly = true)
    public CustomerResponse getById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khách hàng id=" + id));
        return CustomerResponse.from(customer);
    }

    @Transactional(readOnly = true)
    public Page<CustomerResponse> list(int page, int size) {
        Page<Customer> customers = customerRepository.findAll(
                PageRequest.of(page, size, Sort.by("id").ascending()));
        return customers.map(CustomerResponse::from);
    }
}
