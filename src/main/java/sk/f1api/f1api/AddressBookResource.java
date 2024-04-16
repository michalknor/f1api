package sk.f1api.f1api;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AddressBookResource {

	ConcurrentMap<String, Contact> contacts = new ConcurrentHashMap<>();
	
	@GetMapping("/{id}")
	public Contact getContact(@PathVariable String id) {
		return contacts.get(id);
	}

	@GetMapping("/")
	public List<Contact> getAllContacts() {
		return new ArrayList<Contact>(contacts.values());
	}

	@PostMapping("/")
	public Contact addcontact(@RequestBody Contact contact) {
		contacts.put(contact.getId(), contact);
		return contact;
	}
}
