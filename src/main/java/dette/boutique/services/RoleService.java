package dette.boutique.services;

import java.util.List;

import dette.boutique.core.Item;
import dette.boutique.data.entities.Role;
import dette.boutique.data.repository.RoleRepository;

public class RoleService implements Item<Role> {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void create(Role element) {
        roleRepository.insert(element);
    }

    @Override
    public List<Role> list() {
        return roleRepository.selectAll();
    }

}
